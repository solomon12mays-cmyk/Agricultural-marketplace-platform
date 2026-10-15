package com.agricultural.marketplace.logistics.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Assignment of a driver and vehicle to an order's transport.
 */
@Entity
@Table(name = "logistics_assignments")
public class LogisticsAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "pickup_address", length = 255)
    private String pickupAddress;

    @Column(name = "dropoff_address", length = 255)
    private String dropoffAddress;

    @Column(name = "quoted_price", precision = 19, scale = 2)
    private BigDecimal quotedPrice;

    @Column(name = "currency", length = 8)
    private String currency;

    @Column(name = "distance_km", precision = 10, scale = 2)
    private BigDecimal distanceKm;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected LogisticsAssignment() {
        // for JPA
    }

    public LogisticsAssignment(Long orderId, String status) {
        this.orderId = orderId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public LogisticsAssignment(Long orderId, String pickupAddress, String dropoffAddress,
                               BigDecimal quotedPrice, String currency, BigDecimal distanceKm, String status) {
        this.orderId = orderId;
        this.pickupAddress = pickupAddress;
        this.dropoffAddress = dropoffAddress;
        this.quotedPrice = quotedPrice;
        this.currency = currency;
        this.distanceKm = distanceKm;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public void assignDriver(Long driverId, Long vehicleId) {
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.status = "ASSIGNED";
        this.assignedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getStatus() {
        return status;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public String getDropoffAddress() {
        return dropoffAddress;
    }

    public BigDecimal getQuotedPrice() {
        return quotedPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
