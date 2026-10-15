package com.agricultural.marketplace.driver.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateVehicleRequest(
        @NotBlank(message = "Plate number is required")
        @Size(max = 32, message = "Plate number must not exceed 32 characters")
        String plateNumber,

        @NotBlank(message = "Vehicle type is required")
        @Size(max = 64, message = "Vehicle type must not exceed 64 characters")
        String vehicleType,

        @NotNull(message = "Capacity in kg is required")
        @DecimalMin(value = "0.01", message = "Capacity must be greater than zero")
        BigDecimal capacityKg,

        LocalDateTime insuranceExpiresAt
) {
}

