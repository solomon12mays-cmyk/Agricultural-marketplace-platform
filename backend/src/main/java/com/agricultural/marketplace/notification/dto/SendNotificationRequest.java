package com.agricultural.marketplace.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SendNotificationRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotBlank(message = "Channel is required (e.g. SMS, PUSH, USSD)")
        @Size(max = 16, message = "Channel must not exceed 16 characters")
        String channel,

        @NotBlank(message = "Template is required")
        @Size(max = 64, message = "Template must not exceed 64 characters")
        String template,

        @Size(max = 255, message = "Subject must not exceed 255 characters")
        String subject,

        @NotBlank(message = "Body is required")
        @Size(max = 2000, message = "Body must not exceed 2000 characters")
        String body,

        String recipientAddress
) {
}

