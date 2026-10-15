package com.agricultural.marketplace.listing.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public record UpdateListingRequest(
        @DecimalMin("0.001") BigDecimal quantityAvailable,
        @DecimalMin("0.01") BigDecimal pricePerUnit,
        @Size(max = 64) String region) {
}

