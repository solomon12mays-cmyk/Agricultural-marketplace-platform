package com.agricultural.marketplace.logistics.dto;

import jakarta.validation.constraints.NotNull;

public record AssignDriverRequest(
        @NotNull(message = "Driver ID is required")
        Long driverId,

        Long vehicleId
) {
}

