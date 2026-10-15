package com.agricultural.marketplace.operator.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOperatorTaskRequest(
        @NotBlank(message = "Task type is required")
        @Size(max = 64, message = "Task type must not exceed 64 characters")
        String taskType,

        @Size(max = 32, message = "Reference type must not exceed 32 characters")
        String referenceType,

        @Size(max = 64, message = "Reference ID must not exceed 64 characters")
        String referenceId,

        LocalDateTime dueAt
) {
}

