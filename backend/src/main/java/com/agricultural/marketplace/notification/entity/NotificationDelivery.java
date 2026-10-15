package com.agricultural.marketplace.notification.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A delivery attempt for a notification through a specific provider.
 */
@Entity
@Table(name = "notification_deliveries")
public class NotificationDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notification_id", nullable = false)
    private Long notificationId;

    @Column(name = "provider", length = 32)
    private String provider;

    @Column(name = "provider_reference", length = 128)
    private String providerReference;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "attempt", nullable = false)
    private int attempt;

    @Column(name = "error", length = 500)
    private String error;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected NotificationDelivery() {
        // for JPA
    }

    public NotificationDelivery(Long notificationId, String provider, String status, int attempt) {
        this.notificationId = notificationId;
        this.provider = provider;
        this.status = status;
        this.attempt = attempt;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public String getProvider() {
        return provider;
    }

    public String getProviderReference() {
        return providerReference;
    }

    public String getStatus() {
        return status;
    }

    public int getAttempt() {
        return attempt;
    }

    public String getError() {
        return error;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
