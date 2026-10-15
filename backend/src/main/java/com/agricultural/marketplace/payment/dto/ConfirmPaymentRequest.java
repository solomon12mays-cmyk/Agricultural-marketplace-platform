package com.agricultural.marketplace.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfirmPaymentRequest(
        @NotBlank @Size(max = 128) String gatewayReference,
        @Size(max = 32) String method) {
}

