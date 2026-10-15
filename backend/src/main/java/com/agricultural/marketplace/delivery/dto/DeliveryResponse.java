package com.agricultural.marketplace.delivery.dto;

import java.time.LocalDateTime;

import com.agricultural.marketplace.delivery.entity.DeliveryRecord;

public record DeliveryResponse(
        Long id,
        Long orderId,
        Long assignmentId,
        Long driverId,
        String status,
        LocalDateTime deliveredAt,
        String receivedBy,
        String proofNote,
        String conditionNote,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static DeliveryResponse fromEntity(DeliveryRecord record) {
        return new DeliveryResponse(
                record.getId(),
                record.getOrderId(),
                record.getAssignmentId(),
                record.getDriverId(),
                record.getStatus(),
                record.getDeliveredAt(),
                record.getReceivedBy(),
                record.getProofNote(),
                record.getConditionNote(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}

