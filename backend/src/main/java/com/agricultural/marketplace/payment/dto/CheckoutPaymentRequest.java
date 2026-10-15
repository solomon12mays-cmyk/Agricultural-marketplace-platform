package com.agricultural.marketplace.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CheckoutPaymentRequest(
        @NotNull Long orderId,
        @NotBlank @Size(max = 32) String payerType,
        @NotNull Long payerId,
        @Size(max = 32) String method,
        @Size(max = 32) String gateway) {
}

