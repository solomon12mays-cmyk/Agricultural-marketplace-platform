package com.agricultural.marketplace.user.dto;

import java.time.LocalDateTime;

import com.agricultural.marketplace.auth.entity.UserSession;

public record UserSessionResponse(
        Long id,
        Long userId,
        LocalDateTime expiresAt,
        LocalDateTime revokedAt,
        LocalDateTime createdAt,
        boolean active
) {
    public static UserSessionResponse fromEntity(UserSession session) {
        return new UserSessionResponse(
                session.getId(),
                session.getUserId(),
                session.getExpiresAt(),
                session.getRevokedAt(),
                session.getCreatedAt(),
                session.isActive()
        );
    }
}

