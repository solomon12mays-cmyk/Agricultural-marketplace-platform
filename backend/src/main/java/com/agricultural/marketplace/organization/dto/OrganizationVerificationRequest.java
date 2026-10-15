package com.agricultural.marketplace.organization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OrganizationVerificationRequest(
        @NotBlank(message = "Status is required (e.g. UNDER_REVIEW, VERIFIED, REJECTED, SUSPENDED, ACTIVE)")
        String status,

        @Size(max = 500, message = "Notes must not exceed 500 characters")
        String notes
) {
}

