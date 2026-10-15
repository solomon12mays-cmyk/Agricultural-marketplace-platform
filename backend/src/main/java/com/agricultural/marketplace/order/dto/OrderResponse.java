package com.agricultural.marketplace.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.agricultural.marketplace.order.entity.Order;

public record OrderResponse(
        Long id,
        String orderNumber,
        String buyerType,
        Long buyerId,
        String status,
        String paymentStatus,
        BigDecimal totalAmount,
        String currency,
        String deliveryRegion,
        String deliveryAddress,
        String notes,
        LocalDateTime placedAt,
        List<OrderItemResponse> items) {

    public static OrderResponse of(Order order, List<OrderItemResponse> items) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getBuyerType(),
                order.getBuyerId(),
                order.getStatus(),
                order.getPaymentStatus(),
                order.getTotalAmount(),
                order.getCurrency(),
                order.getDeliveryRegion(),
                order.getDeliveryAddress(),
                order.getNotes(),
                order.getPlacedAt(),
                items);
    }
}

