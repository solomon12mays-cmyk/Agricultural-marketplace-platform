package com.agricultural.marketplace.listing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateListingRequest(
        @NotBlank @Size(max = 32) String sellerType,
        @NotNull Long sellerId,
        @NotNull Long productId,
        @NotNull @DecimalMin("0.001") BigDecimal quantityAvailable,
        @NotBlank @Size(max = 16) String unit,
        @NotNull @DecimalMin("0.01") BigDecimal pricePerUnit,
        @Size(max = 8) String currency,
        @Size(max = 64) String region,
        LocalDateTime startsAt,
        LocalDateTime endsAt) {
}

