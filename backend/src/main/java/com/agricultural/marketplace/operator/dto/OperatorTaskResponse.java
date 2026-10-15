package com.agricultural.marketplace.operator.dto;

import java.time.LocalDateTime;

import com.agricultural.marketplace.operator.entity.OperatorTask;

public record OperatorTaskResponse(
        Long id,
        String taskType,
        String status,
        Long assigneeId,
        String referenceType,
        String referenceId,
        LocalDateTime dueAt,
        LocalDateTime completedAt,
        LocalDateTime createdAt
) {
    public static OperatorTaskResponse fromEntity(OperatorTask task) {
        return new OperatorTaskResponse(
                task.getId(),
                task.getTaskType(),
                task.getStatus(),
                task.getAssigneeId(),
                task.getReferenceType(),
                task.getReferenceId(),
                task.getDueAt(),
                task.getCompletedAt(),
                task.getCreatedAt()
        );
    }
}

