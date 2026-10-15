package com.agricultural.marketplace.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfirmDeliveryRequest(
        Long assignmentId,
        Long driverId,

        @NotBlank(message = "Received by name is required")
        @Size(max = 64, message = "Received by must not exceed 64 characters")
        String receivedBy,

        @Size(max = 500, message = "Proof note must not exceed 500 characters")
        String proofNote,

        @Size(max = 500, message = "Condition note must not exceed 500 characters")
        String conditionNote
) {
}

