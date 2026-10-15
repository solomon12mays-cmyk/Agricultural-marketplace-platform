package com.agricultural.marketplace.shared.audit;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Immutable record of a significant action performed on the platform.
 */
@Entity
@Table(name = "audit_events")
public class AuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_type", nullable = false, length = 128)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false, length = 64)
    private String aggregateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 64)
    private AuditAction action;

    @Column(name = "actor_id", length = 64)
    private String actorId;

    @Column(name = "details", length = 2000)
    private String details;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected AuditEvent() {
        // for JPA
    }

    public AuditEvent(String aggregateType, String aggregateId, AuditAction action,
                      String actorId, String details, LocalDateTime createdAt) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.action = action;
        this.actorId = actorId;
        this.details = details;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public AuditAction getAction() {
        return action;
    }

    public String getActorId() {
        return actorId;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
