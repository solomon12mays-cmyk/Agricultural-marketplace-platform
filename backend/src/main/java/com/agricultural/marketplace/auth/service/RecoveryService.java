package com.agricultural.marketplace.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.agricultural.marketplace.auth.dto.PasswordRecoveryRequest;
import com.agricultural.marketplace.auth.entity.User;
import com.agricultural.marketplace.auth.repository.UserRepository;
import com.agricultural.marketplace.auth.repository.UserSessionRepository;
import com.agricultural.marketplace.shared.audit.AuditAction;
import com.agricultural.marketplace.shared.audit.AuditService;
import com.agricultural.marketplace.shared.exception.UnauthorizedException;
import com.agricultural.marketplace.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Handles lost-password recovery: issues a single-use, time-limited
 * recovery code, delivers it over SMS (logged while no provider is
 * configured) and applies the new password with session revocation.
 */
@Service
public class RecoveryService {

    private static final Logger log = LoggerFactory.getLogger(RecoveryService.class);

    private record RecoveryCode(String codeHash, LocalDateTime expiresAt, boolean used) {
    }

    private static final Duration CODE_TTL = Duration.ofMinutes(10);
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int CODE_LENGTH = 6;

    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordService passwordService;
    private final AuditService auditService;
    private final com.agricultural.marketplace.notification.provider.SmsService smsService;
    private final ConcurrentMap<String, RecoveryCode> recoveryCodes = new ConcurrentHashMap<>();

    public RecoveryService(UserRepository userRepository,
                           UserSessionRepository userSessionRepository,
                           PasswordService passwordService,
                           AuditService auditService,
                           com.agricultural.marketplace.notification.provider.SmsService smsService) {
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.passwordService = passwordService;
        this.auditService = auditService;
        this.smsService = smsService;
    }

    /**
     * Resolves the account by phone or email and dispatches a recovery code.
     * The response is always accepted to avoid account enumeration.
     */
    @Transactional
    public void requestRecovery(PasswordRecoveryRequest request) {
        // Clean up expired codes first so entries do not accumulate.
        recoveryCodes.entrySet().removeIf(entry -> entry.getValue().expiresAt().isBefore(LocalDateTime.now()));

        User user = resolveAccount(request.identifier());
        if (user == null) {
            log.debug("Password recovery requested for an unknown identifier");
            return; // Never disclose whether the account exists.
        }

        String code = String.format("%0" + CODE_LENGTH + "d", RANDOM.nextInt((int) Math.pow(10, CODE_LENGTH)));
        String identifierKey = request.identifier().trim().toLowerCase(Locale.ROOT);
        recoveryCodes.put(identifierKey, new RecoveryCode(hash(code), LocalDateTime.now().plus(CODE_TTL), false));
        auditService.record("USER", Long.toString(user.getId()), AuditAction.PASSWORD_CHANGE,
                null, "Password recovery code issued");

        deliverRecoveryCode(user, code);
    }

    /**
     * Validates the recovery code and applies the new password with
     * revocation of every active session for the account.
     */
    @Transactional
    public void resetPassword(String identifier, String recoveryToken, String newPassword) {
        if (!StringUtils.hasText(identifier) || !StringUtils.hasText(recoveryToken)) {
            throw new ValidationException("Recovery identifier and code are required");
        }
        if (!passwordService.isStrongEnough(newPassword)) {
            throw new ValidationException("Password does not meet the policy",
                    java.util.Map.of("newPassword", "Must be at least 10 characters"));
        }
        String identifierKey = identifier.trim().toLowerCase(Locale.ROOT);
        RecoveryCode issued = recoveryCodes.get(identifierKey);
        if (issued == null || issued.used() || issued.expiresAt().isBefore(LocalDateTime.now())
                || !MessageDigest.isEqual(issued.codeHash().getBytes(StandardCharsets.UTF_8),
                        hash(recoveryToken).getBytes(StandardCharsets.UTF_8))) {
            throw new UnauthorizedException("This recovery code is invalid or has expired");
        }

        User user = resolveAccount(identifier);
        if (user == null) {
            throw new UnauthorizedException("This recovery code is invalid or has expired");
        }
        recoveryCodes.put(identifierKey, new RecoveryCode(issued.codeHash(), issued.expiresAt(), true));

        user.setPasswordHash(passwordService.encode(newPassword));
        user.setPasswordChangeRequired(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        userSessionRepository.deleteByUserId(user.getId());
        auditService.record("USER", Long.toString(user.getId()), AuditAction.PASSWORD_CHANGE,
                null, "Password reset through recovery flow");
    }

    private User resolveAccount(String identifier) {
        if (!StringUtils.hasText(identifier)) {
            return null;
        }
        String value = identifier.trim();
        if (value.chars().allMatch(Character::isDigit) || value.startsWith("+")) {
            return userRepository.findByPhone(value).orElse(null);
        }
        return userRepository.findByEmailIgnoreCase(value).orElse(null);
    }

    private void deliverRecoveryCode(User user, String code) {
        String destination = StringUtils.hasText(user.getPhone()) ? user.getPhone() : user.getEmail();
        try {
            smsService.send(destination, "Your password recovery code is " + code);
        } catch (RuntimeException e) {
            // No SMS provider is configured yet; logging the code keeps the
            // flow testable in local environments while remaining explicit.
            log.info("SMS provider not configured; recovery code for account {}: {}", user.getUsername(), code);
        }
    }

    private static String hash(String value) {
        try {
            return Base64.getEncoder().encodeToString(
                    java.security.MessageDigest.getInstance("SHA-256")
                            .digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable", e);
        }
    }
}
