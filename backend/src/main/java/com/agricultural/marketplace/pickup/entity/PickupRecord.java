package com.agricultural.marketplace.pickup.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Record of goods collected from a seller/farm.
 */
@Entity
@Table(name = "pickup_records")
public class PickupRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(name = "farmer_id")
    private Long farmerId;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "picked_up_at")
    private LocalDateTime pickedUpAt;

    @Column(name = "location_note", length = 255)
    private String locationNote;

    @Column(name = "received_by", length = 64)
    private String receivedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected PickupRecord() {
        // for JPA
    }

    public PickupRecord(Long orderId, String status) {
        this.orderId = orderId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public PickupRecord(Long orderId, Long assignmentId, Long farmerId,
                        LocalDateTime scheduledAt, String locationNote, String status) {
        this.orderId = orderId;
        this.assignmentId = assignmentId;
        this.farmerId = farmerId;
        this.scheduledAt = scheduledAt;
        this.locationNote = locationNote;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public void markPickedUp(String receivedBy) {
        this.receivedBy = receivedBy;
        this.pickedUpAt = LocalDateTime.now();
        this.status = "PICKED_UP";
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

    public Long getFarmerId() {
        return farmerId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public LocalDateTime getPickedUpAt() {
        return pickedUpAt;
    }

    public String getLocationNote() {
        return locationNote;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
