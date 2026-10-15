package com.agricultural.marketplace.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.agricultural.marketplace.payment.entity.Payment;

public record PaymentResponse(
        Long id,
        Long orderId,
        String payerType,
        Long payerId,
        BigDecimal amount,
        String currency,
        String method,
        String status,
        String gateway,
        String gatewayReference,
        LocalDateTime createdAt,
        LocalDateTime paidAt) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getPayerType(),
                payment.getPayerId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getGateway(),
                payment.getGatewayReference(),
                payment.getCreatedAt(),
                payment.getPaidAt());
    }
}

