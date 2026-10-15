package com.agricultural.marketplace.personalbuyer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePersonalBuyerRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 128, message = "Full name must not exceed 128 characters")
        String fullName,

        @Email(message = "Invalid email format")
        @Size(max = 128, message = "Email must not exceed 128 characters")
        String email,

        @Size(max = 64, message = "Region must not exceed 64 characters")
        String region
) {
}

