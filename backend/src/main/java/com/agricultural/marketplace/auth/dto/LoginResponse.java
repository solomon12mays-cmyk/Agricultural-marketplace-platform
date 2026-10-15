package com.agricultural.marketplace.auth.dto;

/**
 * Tokens and session details returned after a successful login.
 */
public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInSeconds,
        String username,
        boolean passwordChangeRequired) {

    public static LoginResponse bearer(String accessToken, String refreshToken,
                                       long expiresInSeconds, String username,
                                       boolean passwordChangeRequired) {
        return new LoginResponse(accessToken, refreshToken, "Bearer", expiresInSeconds,
                username, passwordChangeRequired);
    }
}
