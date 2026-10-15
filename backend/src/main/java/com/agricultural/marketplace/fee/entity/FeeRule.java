package com.agricultural.marketplace.fee.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Configuration of a platform fee (commission, delivery, service fee).
 */
@Entity
@Table(name = "fee_rules")
public class FeeRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "fee_type", nullable = false, length = 32)
    private String feeType;

    @Column(name = "percentage", precision = 7, scale = 4)
    private BigDecimal percentage;

    @Column(name = "fixed_amount", precision = 19, scale = 2)
    private BigDecimal fixedAmount;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    @Column(name = "applies_to", length = 32)
    private String appliesTo;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected FeeRule() {
        // for JPA
    }

    public FeeRule(String name, String feeType, String currency) {
        this(name, feeType, null, null, currency, null);
    }

    public FeeRule(String name, String feeType, BigDecimal percentage, BigDecimal fixedAmount, String currency, String appliesTo) {
        this.name = name;
        this.feeType = feeType;
        this.percentage = percentage;
        this.fixedAmount = fixedAmount;
        this.currency = currency;
        this.appliesTo = appliesTo;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFeeType() {
        return feeType;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAppliesTo() {
        return appliesTo;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
