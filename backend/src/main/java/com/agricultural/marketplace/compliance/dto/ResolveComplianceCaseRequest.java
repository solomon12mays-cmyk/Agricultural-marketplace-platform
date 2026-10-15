package com.agricultural.marketplace.compliance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResolveComplianceCaseRequest(
        @NotBlank(message = "Resolution details are required")
        @Size(max = 2000, message = "Resolution must not exceed 2000 characters")
        String resolution,

        String status
) {
}

