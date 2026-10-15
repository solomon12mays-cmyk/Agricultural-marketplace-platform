package com.agricultural.marketplace.product.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A tradeable agricultural product (teff, maize, coffee, ...).
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "slug", nullable = false, unique = true, length = 128)
    private String slug;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "category", length = 64)
    private String category;

    @Column(name = "unit", nullable = false, length = 16)
    private String unit;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "created_by", length = 64)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Product() {
        // for JPA
    }

    public Product(String name, String slug, String unit, String status) {
        this(name, slug, null, null, unit, status, null);
    }

    public Product(String name, String slug, String description, String category, String unit, String status, String createdBy) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.category = category;
        this.unit = unit;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getUnit() {
        return unit;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
