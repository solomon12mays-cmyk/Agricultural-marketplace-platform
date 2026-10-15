package com.agricultural.marketplace.listing.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * An offer of stock for sale on the marketplace.
 */
@Entity
@Table(name = "listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_type", nullable = false, length = 32)
    private String sellerType;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity_available", nullable = false, precision = 19, scale = 3)
    private BigDecimal quantityAvailable;

    @Column(name = "unit", nullable = false, length = 16)
    private String unit;

    @Column(name = "price_per_unit", nullable = false, precision = 19, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "region", length = 64)
    private String region;

    @Column(name = "starts_at")
    private LocalDateTime startsAt;

    @Column(name = "ends_at")
    private LocalDateTime endsAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Listing() {
        // for JPA
    }

    public Listing(String sellerType, Long sellerId, Long productId,
                   BigDecimal quantityAvailable, String unit,
                   BigDecimal pricePerUnit, String currency, String status) {
        this(sellerType, sellerId, productId, quantityAvailable, unit, pricePerUnit, currency, status, null, null, null);
    }

    public Listing(String sellerType, Long sellerId, Long productId,
                   BigDecimal quantityAvailable, String unit,
                   BigDecimal pricePerUnit, String currency, String status,
                   String region, LocalDateTime startsAt, LocalDateTime endsAt) {
        this.sellerType = sellerType;
        this.sellerId = sellerId;
        this.productId = productId;
        this.quantityAvailable = quantityAvailable;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.currency = currency;
        this.status = status;
        this.region = region;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.createdAt = LocalDateTime.now();
    }

    public void update(BigDecimal quantityAvailable, BigDecimal pricePerUnit, String region) {
        if (quantityAvailable != null) {
            this.quantityAvailable = quantityAvailable;
        }
        if (pricePerUnit != null) {
            this.pricePerUnit = pricePerUnit;
        }
        if (region != null) {
            this.region = region;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void deductQuantity(BigDecimal quantity) {
        this.quantityAvailable = this.quantityAvailable.subtract(quantity);
        if (this.quantityAvailable.compareTo(BigDecimal.ZERO) <= 0) {
            this.quantityAvailable = BigDecimal.ZERO;
            this.status = "SOLD_OUT";
        }
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getSellerType() {
        return sellerType;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public Long getProductId() {
        return productId;
    }

    public BigDecimal getQuantityAvailable() {
        return quantityAvailable;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }

    public String getRegion() {
        return region;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
