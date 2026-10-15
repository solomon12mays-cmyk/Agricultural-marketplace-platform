package com.agricultural.marketplace.order.dto;

import java.math.BigDecimal;

import com.agricultural.marketplace.order.entity.OrderItem;

public record OrderItemResponse(
        Long id,
        Long listingId,
        Long productId,
        String productName,
        BigDecimal quantity,
        String unit,
        BigDecimal unitPrice,
        BigDecimal lineTotal) {

    public static OrderItemResponse of(OrderItem item, String productName) {
        return new OrderItemResponse(
                item.getId(),
                item.getListingId(),
                item.getProductId(),
                productName,
                item.getQuantity(),
                item.getUnit(),
                item.getUnitPrice(),
                item.getLineTotal());
    }
}

