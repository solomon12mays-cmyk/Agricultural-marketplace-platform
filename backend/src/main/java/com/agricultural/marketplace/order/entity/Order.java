package com.agricultural.marketplace.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A purchase order placed by a buyer.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true, length = 64)
    private String orderNumber;

    @Column(name = "buyer_type", nullable = false, length = 32)
    private String buyerType;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "payment_status", nullable = false, length = 32)
    private String paymentStatus;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    @Column(name = "delivery_region", length = 64)
    private String deliveryRegion;

    @Column(name = "delivery_address", length = 255)
    private String deliveryAddress;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "placed_at", nullable = false)
    private LocalDateTime placedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Order() {
        // for JPA
    }

    public Order(String orderNumber, String buyerType, Long buyerId,
                 BigDecimal totalAmount, String currency) {
        this.orderNumber = orderNumber;
        this.buyerType = buyerType;
        this.buyerId = buyerId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.status = "PENDING";
        this.paymentStatus = "UNPAID";
        this.placedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getBuyerType() {
        return buyerType;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDeliveryInfo(String deliveryRegion, String deliveryAddress, String notes) {
        this.deliveryRegion = deliveryRegion;
        this.deliveryAddress = deliveryAddress;
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDeliveryRegion() {
        return deliveryRegion;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getPlacedAt() {
        return placedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
