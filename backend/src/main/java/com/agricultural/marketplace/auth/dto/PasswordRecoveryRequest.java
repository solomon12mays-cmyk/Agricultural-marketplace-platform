package com.agricultural.marketplace.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Starts password recovery for an account identified by phone or email.
 */
public record PasswordRecoveryRequest(
        @NotBlank String identifier,
        String channel) {
}
