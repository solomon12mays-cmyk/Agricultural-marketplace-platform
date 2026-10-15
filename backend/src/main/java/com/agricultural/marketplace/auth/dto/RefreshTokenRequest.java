package com.agricultural.marketplace.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Submitted to exchange a refresh token for a new access token.
 */
public record RefreshTokenRequest(
        @NotBlank String refreshToken) {
}
