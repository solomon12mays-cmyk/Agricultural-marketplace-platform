package com.agricultural.marketplace.driver.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.agricultural.marketplace.driver.entity.Vehicle;

public record VehicleResponse(
        Long id,
        Long driverId,
        String plateNumber,
        String vehicleType,
        BigDecimal capacityKg,
        LocalDateTime insuranceExpiresAt,
        String status,
        LocalDateTime createdAt
) {
    public static VehicleResponse fromEntity(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getDriverId(),
                vehicle.getPlateNumber(),
                vehicle.getVehicleType(),
                vehicle.getCapacityKg(),
                vehicle.getInsuranceExpiresAt(),
                vehicle.getStatus(),
                vehicle.getCreatedAt()
        );
    }
}

