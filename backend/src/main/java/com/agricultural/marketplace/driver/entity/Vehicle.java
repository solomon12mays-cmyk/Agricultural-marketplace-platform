package com.agricultural.marketplace.driver.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A vehicle registered by a driver.
 */
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    @Column(name = "plate_number", nullable = false, unique = true, length = 32)
    private String plateNumber;

    @Column(name = "vehicle_type", nullable = false, length = 64)
    private String vehicleType;

    @Column(name = "capacity_kg", precision = 10, scale = 2)
    private BigDecimal capacityKg;

    @Column(name = "insurance_expires_at")
    private LocalDateTime insuranceExpiresAt;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Vehicle() {
        // for JPA
    }

    public Vehicle(Long driverId, String plateNumber, String vehicleType, String status) {
        this(driverId, plateNumber, vehicleType, null, status);
    }

    public Vehicle(Long driverId, String plateNumber, String vehicleType, BigDecimal capacityKg, String status) {
        this.driverId = driverId;
        this.plateNumber = plateNumber;
        this.vehicleType = vehicleType;
        this.capacityKg = capacityKg;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public BigDecimal getCapacityKg() {
        return capacityKg;
    }

    public LocalDateTime getInsuranceExpiresAt() {
        return insuranceExpiresAt;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
