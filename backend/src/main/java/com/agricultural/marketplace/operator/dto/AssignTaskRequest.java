package com.agricultural.marketplace.operator.dto;

import jakarta.validation.constraints.NotNull;

public record AssignTaskRequest(
        @NotNull(message = "Assignee ID is required")
        Long assigneeId
) {
}

