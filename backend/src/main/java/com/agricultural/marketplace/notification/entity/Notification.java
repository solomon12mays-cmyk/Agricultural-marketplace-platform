package com.agricultural.marketplace.notification.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A notification queued for a user.
 */
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "channel", nullable = false, length = 16)
    private String channel;

    @Column(name = "template", nullable = false, length = 64)
    private String template;

    @Column(name = "subject", length = 255)
    private String subject;

    @Column(name = "body", length = 2000)
    private String body;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Notification() {
        // for JPA
    }

    public Notification(Long userId, String channel, String template, String status) {
        this.userId = userId;
        this.channel = channel;
        this.template = template;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Notification(Long userId, String channel, String template, String subject, String body, String status) {
        this.userId = userId;
        this.channel = channel;
        this.template = template;
        this.subject = subject;
        this.body = body;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public void markSent() {
        this.status = "SENT";
        this.sentAt = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = "FAILED";
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getChannel() {
        return channel;
    }

    public String getTemplate() {
        return template;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
