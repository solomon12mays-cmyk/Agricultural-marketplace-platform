package com.agricultural.marketplace.organization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateOrganizationRequest(
        Long userId,

        @NotBlank(message = "Organization name is required")
        @Size(max = 128, message = "Name must not exceed 128 characters")
        String name,

        @NotBlank(message = "Tax ID / TIN is required")
        @Size(max = 64, message = "Tax ID must not exceed 64 characters")
        String taxId,

        @Size(max = 64, message = "Registration number must not exceed 64 characters")
        String registrationNumber,

        @Pattern(regexp = "^\\+?251[0-9]{9}$|^0[0-9]{9}$", message = "Phone must be a valid Ethiopian phone number")
        String phone,

        @Email(message = "Invalid email format")
        @Size(max = 128, message = "Email must not exceed 128 characters")
        String email
) {
}

