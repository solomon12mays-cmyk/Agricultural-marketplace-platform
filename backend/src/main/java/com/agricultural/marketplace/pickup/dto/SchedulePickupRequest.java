package com.agricultural.marketplace.pickup.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SchedulePickupRequest(
        @NotNull(message = "Order ID is required")
        Long orderId,

        Long assignmentId,
        Long farmerId,

        @NotNull(message = "Scheduled time is required")
        LocalDateTime scheduledAt,

        @Size(max = 255, message = "Location note must not exceed 255 characters")
        String locationNote
) {
}

