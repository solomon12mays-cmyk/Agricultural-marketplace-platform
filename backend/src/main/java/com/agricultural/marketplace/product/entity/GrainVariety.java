package com.agricultural.marketplace.product.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Reference data describing a grain variety (crop type, region, yield).
 */
@Entity
@Table(name = "grain_varieties")
public class GrainVariety {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "crop_type", nullable = false, length = 64)
    private String cropType;

    @Column(name = "region", length = 64)
    private String region;

    @Column(name = "maturity_days")
    private Integer maturityDays;

    @Column(name = "protein_percent", precision = 5, scale = 2)
    private BigDecimal proteinPercent;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected GrainVariety() {
        // for JPA
    }

    public GrainVariety(String name, String cropType, String region) {
        this(name, cropType, region, null, null, null);
    }

    public GrainVariety(String name, String cropType, String region, Integer maturityDays, BigDecimal proteinPercent, String notes) {
        this.name = name;
        this.cropType = cropType;
        this.region = region;
        this.maturityDays = maturityDays;
        this.proteinPercent = proteinPercent;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCropType() {
        return cropType;
    }

    public String getRegion() {
        return region;
    }

    public Integer getMaturityDays() {
        return maturityDays;
    }

    public BigDecimal getProteinPercent() {
        return proteinPercent;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
