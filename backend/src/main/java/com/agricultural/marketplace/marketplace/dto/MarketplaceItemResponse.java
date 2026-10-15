package com.agricultural.marketplace.marketplace.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MarketplaceItemResponse(
        Long listingId,
        String sellerType,
        Long sellerId,
        Long productId,
        String productName,
        String category,
        BigDecimal quantityAvailable,
        String unit,
        BigDecimal pricePerUnit,
        String currency,
        String region,
        String status,
        double relevanceScore,
        LocalDateTime createdAt) {
}

