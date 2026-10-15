package com.agricultural.marketplace.compliance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OpenComplianceCaseRequest(
        @NotBlank(message = "Case type is required")
        @Size(max = 64, message = "Case type must not exceed 64 characters")
        String caseType,

        @NotBlank(message = "Subject type is required")
        @Size(max = 32, message = "Subject type must not exceed 32 characters")
        String subjectType,

        @NotBlank(message = "Subject ID is required")
        @Size(max = 64, message = "Subject ID must not exceed 64 characters")
        String subjectId,

        @Size(max = 2000, message = "Description must not exceed 2000 characters")
        String description
) {
}

