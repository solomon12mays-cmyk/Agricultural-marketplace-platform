package com.agricultural.marketplace.logistics.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.agricultural.marketplace.logistics.entity.LogisticsAssignment;

public record LogisticsAssignmentResponse(
        Long id,
        Long orderId,
        Long driverId,
        Long vehicleId,
        String status,
        String pickupAddress,
        String dropoffAddress,
        BigDecimal quotedPrice,
        String currency,
        BigDecimal distanceKm,
        LocalDateTime assignedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static LogisticsAssignmentResponse fromEntity(LogisticsAssignment assignment) {
        return new LogisticsAssignmentResponse(
                assignment.getId(),
                assignment.getOrderId(),
                assignment.getDriverId(),
                assignment.getVehicleId(),
                assignment.getStatus(),
                assignment.getPickupAddress(),
                assignment.getDropoffAddress(),
                assignment.getQuotedPrice(),
                assignment.getCurrency(),
                assignment.getDistanceKm(),
                assignment.getAssignedAt(),
                assignment.getCreatedAt(),
                assignment.getUpdatedAt()
        );
    }
}

