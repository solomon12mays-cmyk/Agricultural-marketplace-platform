package com.agricultural.marketplace.shared.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Hashes and verifies credentials using BCrypt.
 *
 * <p>Named explicitly because the auth module has its own
 * {@code PasswordService} facade that delegates to this one.</p>
 */
@Service("sharedPasswordService")
public class PasswordService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    public boolean isStrongEnough(String rawPassword) {
        // TODO: enforce the platform password policy (length, character classes,
        // breach checks) beyond the encoder's 72-byte limit.
        return rawPassword != null && rawPassword.length() >= 10;
    }
}
