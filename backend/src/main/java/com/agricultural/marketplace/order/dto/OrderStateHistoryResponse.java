package com.agricultural.marketplace.order.dto;

import java.time.LocalDateTime;

import com.agricultural.marketplace.order.entity.OrderStateHistory;

public record OrderStateHistoryResponse(
        Long id,
        Long orderId,
        String fromStatus,
        String toStatus,
        String changedBy,
        String reason,
        LocalDateTime changedAt) {

    public static OrderStateHistoryResponse from(OrderStateHistory entity) {
        return new OrderStateHistoryResponse(
                entity.getId(),
                entity.getOrderId(),
                entity.getFromStatus(),
                entity.getToStatus(),
                entity.getChangedBy(),
                entity.getReason(),
                entity.getChangedAt());
    }
}

