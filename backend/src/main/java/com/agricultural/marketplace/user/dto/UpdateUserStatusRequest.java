package com.agricultural.marketplace.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserStatusRequest(
        @NotBlank(message = "Status is required (e.g. ACTIVE, SUSPENDED, LOCKED, INACTIVE)")
        @Size(max = 32, message = "Status must not exceed 32 characters")
        String status
) {
}

