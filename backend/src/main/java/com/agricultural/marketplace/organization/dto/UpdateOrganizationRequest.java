package com.agricultural.marketplace.organization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateOrganizationRequest(
        @NotBlank(message = "Organization name is required")
        @Size(max = 128, message = "Name must not exceed 128 characters")
        String name,

        @Pattern(regexp = "^\\+?251[0-9]{9}$|^0[0-9]{9}$", message = "Phone must be a valid Ethiopian phone number")
        String phone,

        @Email(message = "Invalid email format")
        @Size(max = 128, message = "Email must not exceed 128 characters")
        String email
) {
}

