package com.agricultural.marketplace.payout.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A disbursement of funds to a farmer or driver.
 */
@Entity
@Table(name = "payouts")
public class Payout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payee_type", nullable = false, length = 32)
    private String payeeType;

    @Column(name = "payee_id", nullable = false)
    private Long payeeId;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "period_start")
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    @Column(name = "reference", length = 64)
    private String reference;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Payout() {
        // for JPA
    }

    public Payout(String payeeType, Long payeeId, BigDecimal amount, String currency, String status) {
        this.payeeType = payeeType;
        this.payeeId = payeeId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public void markProcessed(String reference) {
        this.status = "PROCESSED";
        this.reference = reference;
        this.processedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getPayeeType() {
        return payeeType;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public String getReference() {
        return reference;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
