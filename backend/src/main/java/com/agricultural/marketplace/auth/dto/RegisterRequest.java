package com.agricultural.marketplace.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data required to create a new account.
 */
public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 64) String username,
        @NotBlank @Size(max = 128) String email,
        @NotBlank @Size(max = 32) String phone,
        @NotBlank @Size(min = 10, max = 72) String password,
        @NotBlank @Size(max = 128) String fullName) {
}
