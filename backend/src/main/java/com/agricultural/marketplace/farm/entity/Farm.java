package com.agricultural.marketplace.farm.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A farm registered by a farmer.
 */
@Entity
@Table(name = "farms")
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "farmer_id", nullable = false)
    private Long farmerId;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "farm_type", length = 64)
    private String farmType;

    @Column(name = "size_hectares", precision = 10, scale = 2)
    private BigDecimal sizeHectares;

    @Column(name = "certification_status", length = 32)
    private String certificationStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Farm() {
        // for JPA
    }

    public Farm(Long farmerId, String name, String farmType) {
        this(farmerId, name, farmType, null, null);
    }

    public Farm(Long farmerId, String name, String farmType, BigDecimal sizeHectares, String certificationStatus) {
        this.farmerId = farmerId;
        this.name = name;
        this.farmType = farmType;
        this.sizeHectares = sizeHectares;
        this.certificationStatus = certificationStatus;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public String getName() {
        return name;
    }

    public String getFarmType() {
        return farmType;
    }

    public BigDecimal getSizeHectares() {
        return sizeHectares;
    }

    public String getCertificationStatus() {
        return certificationStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
