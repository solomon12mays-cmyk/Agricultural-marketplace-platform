package com.agricultural.marketplace.pickup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfirmPickupRequest(
        @NotBlank(message = "Received by name is required")
        @Size(max = 64, message = "Received by must not exceed 64 characters")
        String receivedBy
) {
}

