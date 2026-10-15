package com.agricultural.marketplace.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;

import com.agricultural.marketplace.auth.dto.ChangePasswordRequest;
import com.agricultural.marketplace.auth.dto.LoginRequest;
import com.agricultural.marketplace.auth.dto.LoginResponse;
import com.agricultural.marketplace.auth.dto.RefreshTokenRequest;
import com.agricultural.marketplace.auth.dto.RegisterRequest;
import com.agricultural.marketplace.auth.entity.User;
import com.agricultural.marketplace.auth.entity.UserSession;
import com.agricultural.marketplace.auth.repository.UserRepository;
import com.agricultural.marketplace.auth.repository.UserRoleRepository;
import com.agricultural.marketplace.auth.repository.UserSessionRepository;
import com.agricultural.marketplace.shared.audit.AuditAction;
import com.agricultural.marketplace.shared.audit.AuditService;
import com.agricultural.marketplace.shared.exception.ConflictException;
import com.agricultural.marketplace.shared.exception.UnauthorizedException;
import com.agricultural.marketplace.shared.exception.ValidationException;
import com.agricultural.marketplace.shared.security.JwtTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Facade over the auth flows (login, registration, refresh, password change).
 */
@Service
public class AuthService {

    private final LoginService loginService;
    private final RegistrationService registrationService;
    private final PasswordService passwordService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserSessionRepository userSessionRepository;
    private final JwtTokenService jwtTokenService;
    private final AuditService auditService;

    public AuthService(LoginService loginService,
                       RegistrationService registrationService,
                       PasswordService passwordService,
                       UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       UserSessionRepository userSessionRepository,
                       JwtTokenService jwtTokenService,
                       AuditService auditService) {
        this.loginService = loginService;
        this.registrationService = registrationService;
        this.passwordService = passwordService;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userSessionRepository = userSessionRepository;
        this.jwtTokenService = jwtTokenService;
        this.auditService = auditService;
    }

    public LoginResponse login(LoginRequest request) {
        return loginService.authenticate(request);
    }

    public Long register(RegisterRequest request) {
        return registrationService.register(request);
    }

    /**
     * Exchanges a valid refresh token for a new token pair, rotating the
     * stored session so a refresh token can only be used once.
     */
    @Transactional
    public LoginResponse refresh(RefreshTokenRequest request) {
        JwtTokenService.Claims claims;
        try {
            claims = jwtTokenService.claimsAllowExpired(request.refreshToken());
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException("Invalid refresh token", e);
        }
        if (!"refresh".equals(claims.tokenUse()) || claims.subject() == null) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        UserSession session = userSessionRepository.findByRefreshTokenHash(hash(request.refreshToken()))
                .orElseThrow(() -> new UnauthorizedException("Refresh token is not recognized"));
        if (session.getRevokedAt() != null) {
            // Reuse of an already-rotated refresh token: revoke the whole chain
            // of the account's active sessions.
            userSessionRepository.deleteByUserIdAndRevokedAtIsNull(session.getUserId());
            auditService.record("USER", Long.toString(session.getUserId()), AuditAction.LOGOUT,
                    null, "Refresh token reuse detected; active sessions revoked");
            throw new UnauthorizedException("Refresh token has already been used");
        }
        if (!session.isActive()) {
            throw new UnauthorizedException("Refresh token has expired");
        }

        User user = userRepository.findById(session.getUserId())
                .orElseThrow(() -> new UnauthorizedException("Refresh token account no longer exists"));

        session.revoke();
        userSessionRepository.save(session);

        List<String> roleCodes = userRoleRepository.findRoleCodesByUserId(user.getId());
        String accessToken = jwtTokenService.createAccessToken(user.getUsername(), roleCodes);
        String refreshToken = jwtTokenService.createRefreshToken(user.getUsername(), null);
        UserSession rotated = new UserSession(
                user.getId(), hash(refreshToken), LocalDateTime.now().plusSeconds(jwtTokenService.refreshTtlSeconds()));
        userSessionRepository.save(rotated);

        return LoginResponse.bearer(accessToken, refreshToken, jwtTokenService.accessTtlSeconds(),
                user.getUsername(), user.isPasswordChangeRequired());
    }

    /**
     * Verifies the current password, stores the new one and revokes all
     * other sessions for the account.
     */
    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        if (!StringUtils.hasText(username)) {
            throw new UnauthorizedException("An authenticated user is required");
        }
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UnauthorizedException("Account not found"));
        if (!passwordService.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new ValidationException("Current password is incorrect",
                    java.util.Map.of("currentPassword", "Incorrect"));
        }
        if (!passwordService.isStrongEnough(request.newPassword())) {
            throw new ValidationException("Password does not meet the policy",
                    java.util.Map.of("newPassword", "Must be at least 10 characters"));
        }
        if (passwordService.matches(request.newPassword(), user.getPasswordHash())) {
            throw new ConflictException("Choose a password you have not used before");
        }
        user.setPasswordHash(passwordService.encode(request.newPassword()));
        user.setPasswordChangeRequired(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        userSessionRepository.deleteByUserId(user.getId());
        auditService.record("USER", Long.toString(user.getId()), AuditAction.PASSWORD_CHANGE,
                username, "Password changed by the account owner");
    }

    static String hash(String refreshToken) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(refreshToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable", e);
        }
    }
}
