package com.agricultural.marketplace.payment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A payment initiated for an order.
 */
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "payer_type", nullable = false, length = 32)
    private String payerType;

    @Column(name = "payer_id", nullable = false)
    private Long payerId;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    @Column(name = "method", length = 32)
    private String method;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "gateway", length = 32)
    private String gateway;

    @Column(name = "gateway_reference", length = 128)
    private String gatewayReference;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    protected Payment() {
        // for JPA
    }

    public Payment(Long orderId, String payerType, Long payerId,
                   BigDecimal amount, String currency, String status) {
        this.orderId = orderId;
        this.payerType = payerType;
        this.payerId = payerId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getPayerType() {
        return payerType;
    }

    public Long getPayerId() {
        return payerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMethod() {
        return method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void markPaid(String gateway, String gatewayReference, String method) {
        this.status = "CAPTURED";
        this.gateway = gateway;
        this.gatewayReference = gatewayReference;
        this.method = method;
        this.paidAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getGateway() {
        return gateway;
    }

    public String getGatewayReference() {
        return gatewayReference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }
}
