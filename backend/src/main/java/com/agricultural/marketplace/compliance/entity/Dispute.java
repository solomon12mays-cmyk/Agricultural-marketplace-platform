package com.agricultural.marketplace.compliance.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A dispute raised by a buyer, seller or driver about an order.
 */
@Entity
@Table(name = "disputes")
public class Dispute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "opened_by", nullable = false, length = 64)
    private String openedBy;

    @Column(name = "respondent_id", length = 64)
    private String respondentId;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "reason", nullable = false, length = 2000)
    private String reason;

    @Column(name = "resolution", length = 2000)
    private String resolution;

    @Column(name = "opened_at", nullable = false)
    private LocalDateTime openedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    protected Dispute() {
        // for JPA
    }

    public Dispute(Long orderId, String openedBy, String status, String reason) {
        this.orderId = orderId;
        this.openedBy = openedBy;
        this.status = status;
        this.reason = reason;
        this.openedAt = LocalDateTime.now();
    }

    public Dispute(Long orderId, String openedBy, String respondentId, String status, String reason) {
        this.orderId = orderId;
        this.openedBy = openedBy;
        this.respondentId = respondentId;
        this.status = status;
        this.reason = reason;
        this.openedAt = LocalDateTime.now();
    }

    public void resolve(String resolution, String status) {
        this.resolution = resolution;
        this.status = status != null ? status : "RESOLVED";
        this.resolvedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOpenedBy() {
        return openedBy;
    }

    public String getRespondentId() {
        return respondentId;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getResolution() {
        return resolution;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }
}
