package com.agricultural.marketplace.notification.dto;

import java.time.LocalDateTime;

import com.agricultural.marketplace.notification.entity.Notification;

public record NotificationResponse(
        Long id,
        Long userId,
        String channel,
        String template,
        String subject,
        String body,
        String status,
        LocalDateTime sentAt,
        LocalDateTime createdAt
) {
    public static NotificationResponse fromEntity(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getUserId(),
                notification.getChannel(),
                notification.getTemplate(),
                notification.getSubject(),
                notification.getBody(),
                notification.getStatus(),
                notification.getSentAt(),
                notification.getCreatedAt()
        );
    }
}

