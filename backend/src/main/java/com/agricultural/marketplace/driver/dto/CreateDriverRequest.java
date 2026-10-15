package com.agricultural.marketplace.driver.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateDriverRequest(
        Long userId,

        @NotBlank(message = "Full name is required")
        @Size(max = 128, message = "Full name must not exceed 128 characters")
        String fullName,

        @NotBlank(message = "License number is required")
        @Size(max = 64, message = "License number must not exceed 64 characters")
        String licenseNumber,

        LocalDateTime licenseExpiresAt,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?251[0-9]{9}$|^0[0-9]{9}$", message = "Phone must be a valid Ethiopian phone number")
        String phone
) {
}

