package com.agricultural.marketplace.auth.service;

import java.time.LocalDateTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

import com.agricultural.marketplace.auth.dto.LoginRequest;
import com.agricultural.marketplace.auth.dto.LoginResponse;
import com.agricultural.marketplace.auth.entity.User;
import com.agricultural.marketplace.auth.repository.UserRepository;
import com.agricultural.marketplace.auth.repository.UserRoleRepository;
import com.agricultural.marketplace.auth.repository.UserSessionRepository;
import com.agricultural.marketplace.auth.entity.UserSession;
import com.agricultural.marketplace.shared.audit.AuditAction;
import com.agricultural.marketplace.shared.audit.AuditService;
import com.agricultural.marketplace.shared.exception.UnauthorizedException;
import com.agricultural.marketplace.shared.security.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authenticates credentials and issues a JWT access/refresh token pair,
 * recording a server-side session for the refresh token.
 */
@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordService passwordService;
    private final AccountLockService accountLockService;
    private final JwtTokenService jwtTokenService;
    private final AuditService auditService;

    public LoginService(UserRepository userRepository,
                        UserRoleRepository userRoleRepository,
                        UserSessionRepository userSessionRepository,
                        PasswordService passwordService,
                        AccountLockService accountLockService,
                        JwtTokenService jwtTokenService,
                        AuditService auditService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userSessionRepository = userSessionRepository;
        this.passwordService = passwordService;
        this.accountLockService = accountLockService;
        this.jwtTokenService = jwtTokenService;
        this.auditService = auditService;
    }

    @Transactional
    public LoginResponse authenticate(LoginRequest request) {
        if (accountLockService.isLocked(request.username())) {
            log.debug("Login attempt for a locked account");
            throw new UnauthorizedException(
                    "Account is temporarily locked after repeated failed sign-in attempts");
        }

        User user = userRepository.findByUsernameIgnoreCase(request.username()).orElse(null);
        boolean passwordMatches = user != null
                && passwordService.matches(request.password(), user.getPasswordHash());

        if (!passwordMatches) {
            accountLockService.registerFailure(request.username());
            auditService.record("USER", request.username(), AuditAction.LOGIN,
                    null, "Failed login attempt");
            throw new UnauthorizedException("Invalid username or password");
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            auditService.record("USER", Long.toString(user.getId()), AuditAction.LOGIN,
                    null, "Sign-in blocked for account status " + user.getStatus());
            throw new UnauthorizedException("This account is not active");
        }

        accountLockService.reset(request.username());
        user.setFailedLoginAttempts(0);
        user.setLastLoginAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        List<String> roleCodes = userRoleRepository.findRoleCodesByUserId(user.getId());
        String accessToken = jwtTokenService.createAccessToken(user.getUsername(), roleCodes);
        String refreshToken = jwtTokenService.createRefreshToken(user.getUsername(), null);

        UserSession session = new UserSession(
                user.getId(), hash(refreshToken), LocalDateTime.now().plusSeconds(jwtTokenService.refreshTtlSeconds()));
        userSessionRepository.save(session);

        auditService.record("USER", Long.toString(user.getId()), AuditAction.LOGIN,
                null, "Login succeeded");

        return LoginResponse.bearer(accessToken, refreshToken, jwtTokenService.accessTtlSeconds(),
                user.getUsername(), user.isPasswordChangeRequired());
    }

    private static String hash(String refreshToken) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(refreshToken.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable", e);
        }
    }
}
