package com.agricultural.marketplace.operator.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A manual work item for platform operators (review, KYC, investigation).
 */
@Entity
@Table(name = "operator_tasks")
public class OperatorTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_type", nullable = false, length = 64)
    private String taskType;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "assignee_id")
    private Long assigneeId;

    @Column(name = "reference_type", length = 32)
    private String referenceType;

    @Column(name = "reference_id", length = 64)
    private String referenceId;

    @Column(name = "due_at")
    private LocalDateTime dueAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected OperatorTask() {
        // for JPA
    }

    public OperatorTask(String taskType, String status) {
        this.taskType = taskType;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public OperatorTask(String taskType, Long assigneeId, String referenceType,
                        String referenceId, LocalDateTime dueAt, String status) {
        this.taskType = taskType;
        this.assigneeId = assigneeId;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.dueAt = dueAt;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public void assignTo(Long assigneeId) {
        this.assigneeId = assigneeId;
        this.status = "ASSIGNED";
    }

    public void complete() {
        this.status = "COMPLETED";
        this.completedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getStatus() {
        return status;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public LocalDateTime getDueAt() {
        return dueAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
