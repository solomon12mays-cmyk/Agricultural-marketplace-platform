package com.agricultural.marketplace.inventory.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdjustStockRequest(
        @NotBlank @Size(max = 32) String ownerType,
        @NotNull Long ownerId,
        @NotNull Long productId,
        @NotNull @DecimalMin("0.001") BigDecimal quantity,
        @NotBlank @Size(max = 32) String type,
        @Size(max = 32) String referenceType,
        @Size(max = 64) String referenceId,
        @Size(max = 16) String unit) {
}

