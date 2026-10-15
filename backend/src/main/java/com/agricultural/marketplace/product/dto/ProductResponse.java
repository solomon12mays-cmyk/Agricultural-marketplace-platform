package com.agricultural.marketplace.product.dto;

import java.time.LocalDateTime;

import com.agricultural.marketplace.product.entity.Product;

public record ProductResponse(
        Long id,
        String name,
        String slug,
        String description,
        String category,
        String unit,
        String status,
        String createdBy,
        LocalDateTime createdAt) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getCategory(),
                product.getUnit(),
                product.getStatus(),
                product.getCreatedBy(),
                product.getCreatedAt());
    }
}

