package com.agricultural.marketplace.pickup.dto;

import java.time.LocalDateTime;

import com.agricultural.marketplace.pickup.entity.PickupRecord;

public record PickupResponse(
        Long id,
        Long orderId,
        Long assignmentId,
        Long farmerId,
        String status,
        LocalDateTime scheduledAt,
        LocalDateTime pickedUpAt,
        String locationNote,
        String receivedBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PickupResponse fromEntity(PickupRecord record) {
        return new PickupResponse(
                record.getId(),
                record.getOrderId(),
                record.getAssignmentId(),
                record.getFarmerId(),
                record.getStatus(),
                record.getScheduledAt(),
                record.getPickedUpAt(),
                record.getLocationNote(),
                record.getReceivedBy(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}

