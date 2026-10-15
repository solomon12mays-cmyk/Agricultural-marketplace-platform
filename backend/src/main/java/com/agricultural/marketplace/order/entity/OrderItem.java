package com.agricultural.marketplace.order.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A single line of an order.
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "listing_id", nullable = false)
    private Long listingId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 3)
    private BigDecimal quantity;

    @Column(name = "unit", nullable = false, length = 16)
    private String unit;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal lineTotal;

    protected OrderItem() {
        // for JPA
    }

    public OrderItem(Long orderId, Long listingId, Long productId,
                     BigDecimal quantity, String unit, BigDecimal unitPrice, BigDecimal lineTotal) {
        this.orderId = orderId;
        this.listingId = listingId;
        this.productId = productId;
        this.quantity = quantity;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getListingId() {
        return listingId;
    }

    public Long getProductId() {
        return productId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }
}
