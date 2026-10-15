package com.agricultural.marketplace.compliance.dto;

import java.time.LocalDateTime;

import com.agricultural.marketplace.compliance.entity.Dispute;

public record DisputeResponse(
        Long id,
        Long orderId,
        String openedBy,
        String respondentId,
        String status,
        String reason,
        String resolution,
        LocalDateTime openedAt,
        LocalDateTime resolvedAt
) {
    public static DisputeResponse fromEntity(Dispute d) {
        return new DisputeResponse(
                d.getId(),
                d.getOrderId(),
                d.getOpenedBy(),
                d.getRespondentId(),
                d.getStatus(),
                d.getReason(),
                d.getResolution(),
                d.getOpenedAt(),
                d.getResolvedAt()
        );
    }
}

