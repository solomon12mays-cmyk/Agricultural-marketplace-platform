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
 * Profile of a transport driver.
 */
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "full_name", nullable = false, length = 128)
    private String fullName;

    @Column(name = "license_number", nullable = false, unique = true, length = 64)
    private String licenseNumber;

    @Column(name = "license_expires_at")
    private LocalDateTime licenseExpiresAt;

    @Column(name = "phone", nullable = false, unique = true, length = 32)
    private String phone;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Driver() {
        // for JPA
    }

    public Driver(String fullName, String licenseNumber, String phone, String status) {
        this(null, fullName, licenseNumber, phone, status);
    }

    public Driver(Long userId, String fullName, String licenseNumber, String phone, String status) {
        this.userId = userId;
        this.fullName = fullName;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.status = status;
        this.rating = new BigDecimal("5.00");
        this.createdAt = LocalDateTime.now();
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public LocalDateTime getLicenseExpiresAt() {
        return licenseExpiresAt;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
