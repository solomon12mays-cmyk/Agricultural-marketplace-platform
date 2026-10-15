package com.agricultural.marketplace.listing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.agricultural.marketplace.listing.entity.Listing;

public record ListingResponse(
        Long id,
        String sellerType,
        Long sellerId,
        Long productId,
        String productName,
        BigDecimal quantityAvailable,
        String unit,
        BigDecimal pricePerUnit,
        String currency,
        String status,
        String region,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        LocalDateTime createdAt) {

    public static ListingResponse of(Listing listing, String productName) {
        return new ListingResponse(
                listing.getId(),
                listing.getSellerType(),
                listing.getSellerId(),
                listing.getProductId(),
                productName,
                listing.getQuantityAvailable(),
                listing.getUnit(),
                listing.getPricePerUnit(),
                listing.getCurrency(),
                listing.getStatus(),
                listing.getRegion(),
                listing.getStartsAt(),
                listing.getEndsAt(),
                listing.getCreatedAt());
    }
}

