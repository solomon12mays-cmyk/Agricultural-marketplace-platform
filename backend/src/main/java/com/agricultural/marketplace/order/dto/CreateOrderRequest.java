package com.agricultural.marketplace.order.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateOrderRequest(
        @NotBlank @Size(max = 32) String buyerType,
        @NotNull Long buyerId,
        @Size(max = 64) String deliveryRegion,
        @Size(max = 255) String deliveryAddress,
        @Size(max = 500) String notes,
        @NotEmpty @Valid List<CreateOrderItemRequest> items) {
}

