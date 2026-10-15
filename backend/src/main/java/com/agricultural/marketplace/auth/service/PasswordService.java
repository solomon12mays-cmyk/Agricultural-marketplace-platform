package com.agricultural.marketplace.auth.service;

import org.springframework.stereotype.Service;

/**
 * Password handling for the auth module, delegating to the shared
 * BCrypt-based implementation.
 */
@Service
public class PasswordService {

    private final com.agricultural.marketplace.shared.security.PasswordService passwordService;

    public PasswordService(com.agricultural.marketplace.shared.security.PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    public String encode(String rawPassword) {
        return passwordService.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordService.matches(rawPassword, encodedPassword);
    }

    public boolean isStrongEnough(String rawPassword) {
        return passwordService.isStrongEnough(rawPassword);
    }
}
