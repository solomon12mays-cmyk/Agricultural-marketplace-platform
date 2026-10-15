package com.agricultural.marketplace.personalbuyer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePersonalBuyerRequest(
        Long userId,

        @NotBlank(message = "Full name is required")
        @Size(max = 128, message = "Full name must not exceed 128 characters")
        String fullName,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?251[0-9]{9}$|^0[0-9]{9}$", message = "Phone must be a valid Ethiopian phone number")
        String phone,

        @Email(message = "Invalid email format")
        @Size(max = 128, message = "Email must not exceed 128 characters")
        String email,

        @Size(max = 64, message = "Region must not exceed 64 characters")
        String region
) {
}

