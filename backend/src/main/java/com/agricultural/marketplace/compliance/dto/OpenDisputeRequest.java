package com.agricultural.marketplace.compliance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OpenDisputeRequest(
        @NotNull(message = "Order ID is required")
        Long orderId,

        @Size(max = 64, message = "Respondent ID must not exceed 64 characters")
        String respondentId,

        @NotBlank(message = "Reason is required")
        @Size(max = 2000, message = "Reason must not exceed 2000 characters")
        String reason
) {
}

