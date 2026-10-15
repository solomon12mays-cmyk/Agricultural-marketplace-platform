package com.agricultural.marketplace.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AssignRoleRequest(
        @NotBlank(message = "Role code is required (e.g. FARMER, BUYER, DRIVER, OPERATOR, ADMIN)")
        @Size(max = 64, message = "Role code must not exceed 64 characters")
        String roleCode
) {
}

