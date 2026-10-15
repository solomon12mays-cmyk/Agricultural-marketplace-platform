package com.agricultural.marketplace.user.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.agricultural.marketplace.auth.entity.User;

public record UserResponse(
        Long id,
        String username,
        String email,
        String phone,
        String status,
        boolean passwordChangeRequired,
        int failedLoginAttempts,
        LocalDateTime lockedUntil,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<String> roles
) {
    public static UserResponse of(User user, List<String> roles) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getStatus(),
                user.isPasswordChangeRequired(),
                user.getFailedLoginAttempts(),
                user.getLockedUntil(),
                user.getLastLoginAt(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                roles != null ? roles : List.of()
        );
    }
}

