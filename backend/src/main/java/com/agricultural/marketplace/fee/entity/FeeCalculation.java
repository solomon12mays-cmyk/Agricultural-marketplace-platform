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
 * A fee computed for a settlement target, kept for auditability.
 */
@Entity
@Table(name = "fee_calculations")
public class FeeCalculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_type", nullable = false, length = 32)
    private String referenceType;

    @Column(name = "reference_id", nullable = false, length = 64)
    private String referenceId;

    @Column(name = "fee_rule_id", nullable = false)
    private Long feeRuleId;

    @Column(name = "base_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal baseAmount;

    @Column(name = "fee_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal feeAmount;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    protected FeeCalculation() {
        // for JPA
    }

    public FeeCalculation(String referenceType, String referenceId, Long feeRuleId,
                          BigDecimal baseAmount, BigDecimal feeAmount, String currency) {
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.feeRuleId = feeRuleId;
        this.baseAmount = baseAmount;
        this.feeAmount = feeAmount;
        this.currency = currency;
        this.calculatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public Long getFeeRuleId() {
        return feeRuleId;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }
}
