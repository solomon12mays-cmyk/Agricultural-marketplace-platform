package com.agricultural.marketplace.delivery.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Record of a completed (or failed) delivery to the buyer.
 */
@Entity
@Table(name = "delivery_records")
public class DeliveryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "received_by", length = 64)
    private String receivedBy;

    @Column(name = "proof_note", length = 500)
    private String proofNote;

    @Column(name = "condition_note", length = 500)
    private String conditionNote;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected DeliveryRecord() {
        // for JPA
    }

    public DeliveryRecord(Long orderId, String status) {
        this.orderId = orderId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public DeliveryRecord(Long orderId, Long assignmentId, Long driverId, String status) {
        this.orderId = orderId;
        this.assignmentId = assignmentId;
        this.driverId = driverId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public void markDelivered(String receivedBy, String proofNote, String conditionNote) {
        this.receivedBy = receivedBy;
        this.proofNote = proofNote;
        this.conditionNote = conditionNote;
        this.deliveredAt = LocalDateTime.now();
        this.status = "DELIVERED";
        this.updatedAt = LocalDateTime.now();
    }

    public void markFailed(String reason) {
        this.conditionNote = reason;
        this.status = "FAILED";
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public String getProofNote() {
        return proofNote;
    }

    public String getConditionNote() {
        return conditionNote;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
