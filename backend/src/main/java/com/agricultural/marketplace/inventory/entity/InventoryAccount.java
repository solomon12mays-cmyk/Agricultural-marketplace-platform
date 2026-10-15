package com.agricultural.marketplace.inventory.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Stock position of an owner (farmer, organization) for one product.
 */
@Entity
@Table(name = "inventory_accounts")
public class InventoryAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_type", nullable = false, length = 32)
    private String ownerType;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 3)
    private BigDecimal quantity;

    @Column(name = "reserved_quantity", nullable = false, precision = 19, scale = 3)
    private BigDecimal reservedQuantity;

    @Column(name = "unit", nullable = false, length = 16)
    private String unit;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected InventoryAccount() {
        // for JPA
    }

    public InventoryAccount(String ownerType, Long ownerId, Long productId, String unit) {
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.productId = productId;
        this.quantity = BigDecimal.ZERO;
        this.reservedQuantity = BigDecimal.ZERO;
        this.unit = unit;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getProductId() {
        return productId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getReservedQuantity() {
        return reservedQuantity;
    }

    public BigDecimal getAvailableQuantity() {
        return quantity.subtract(reservedQuantity);
    }

    public void addStock(BigDecimal amount) {
        this.quantity = this.quantity.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void deductStock(BigDecimal amount) {
        this.quantity = this.quantity.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void reserveStock(BigDecimal amount) {
        this.reservedQuantity = this.reservedQuantity.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void releaseReservation(BigDecimal amount) {
        this.reservedQuantity = this.reservedQuantity.subtract(amount);
        if (this.reservedQuantity.compareTo(BigDecimal.ZERO) < 0) {
            this.reservedQuantity = BigDecimal.ZERO;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void commitReservation(BigDecimal amount) {
        this.quantity = this.quantity.subtract(amount);
        this.reservedQuantity = this.reservedQuantity.subtract(amount);
        if (this.reservedQuantity.compareTo(BigDecimal.ZERO) < 0) {
            this.reservedQuantity = BigDecimal.ZERO;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public String getUnit() {
        return unit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
