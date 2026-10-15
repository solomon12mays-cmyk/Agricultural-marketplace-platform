package com.agricultural.marketplace.logistics.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateLogisticsRequest(
        @NotNull(message = "Order ID is required")
        Long orderId,

        @Size(max = 255, message = "Pickup address must not exceed 255 characters")
        String pickupAddress,

        @Size(max = 255, message = "Dropoff address must not exceed 255 characters")
        String dropoffAddress,

        BigDecimal distanceKm,
        BigDecimal quotedPrice
) {
}

