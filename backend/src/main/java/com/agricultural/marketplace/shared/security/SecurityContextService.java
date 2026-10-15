package com.agricultural.marketplace.shared.security;

import java.util.Optional;

import com.agricultural.marketplace.shared.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Convenience access to the current security context.
 */
@Service
public class SecurityContextService {

    public Optional<Authentication> currentAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    public Optional<String> currentUsername() {
        return currentAuthentication()
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName);
    }

    public String requireCurrentUsername() {
        return currentUsername()
                .orElseThrow(() -> new UnauthorizedException("An authenticated user is required"));
    }

    public void clear() {
        SecurityContextHolder.clearContext();
    }
}
