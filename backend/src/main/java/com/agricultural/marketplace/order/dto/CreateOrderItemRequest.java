package com.agricultural.marketplace.order.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemRequest(
        @NotNull Long listingId,
        @NotNull @DecimalMin("0.001") BigDecimal quantity) {
}

