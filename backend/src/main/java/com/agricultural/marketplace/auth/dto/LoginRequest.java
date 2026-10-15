package com.agricultural.marketplace.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Credentials submitted to obtain tokens.
 */
public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password) {
}
