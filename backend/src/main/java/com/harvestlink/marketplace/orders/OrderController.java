package com.harvestlink.marketplace.orders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orders")
    public List<OrderResponse> getOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(OrderResponse::from)
                .toList();
    }

    @GetMapping("/orders/{id}")
    public OrderResponse getOrder(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(OrderResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @PostMapping("/orders")
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        if (request == null || request.items() == null || request.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must include at least one item");
        }

        if (!StringUtils.hasText(request.buyerName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Buyer name is required");
        }

        if (!StringUtils.hasText(request.buyerEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Buyer email is required");
        }

        if (!StringUtils.hasText(request.shippingAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shipping address is required");
        }

        Order order = new Order(request.buyerName(), request.buyerEmail(), request.shippingAddress(), request.notes());
        if (request.paymentStatus() != null) {
            try {
                order.setPaymentStatus(PaymentStatus.valueOf(request.paymentStatus().trim().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported payment status: " + request.paymentStatus());
            }
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderLineRequest item : request.items()) {
            if (item == null || item.quantity() == null || item.quantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Each order item must include a quantity greater than zero");
            }

            BigDecimal unitPrice = item.price() == null ? BigDecimal.ZERO : item.price();
            totalAmount = totalAmount.add(unitPrice.multiply(BigDecimal.valueOf(item.quantity())));

            order.addItem(new OrderItem(
                    item.productId(),
                    item.name(),
                    item.slug(),
                    item.unit(),
                    unitPrice,
                    item.currency(),
                    item.quantity(),
                    item.sellerName()));
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    @PatchMapping("/orders/{id}/status")
    public OrderResponse updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatusUpdateRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order update payload is required");
        }

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (StringUtils.hasText(request.status())) {
            try {
                order.setStatus(OrderStatus.valueOf(request.status().trim().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported order status: " + request.status());
            }
        }

        if (StringUtils.hasText(request.paymentStatus())) {
            try {
                order.setPaymentStatus(PaymentStatus.valueOf(request.paymentStatus().trim().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported payment status: " + request.paymentStatus());
            }
        }

        return OrderResponse.from(orderRepository.save(order));
    }

    public record CreateOrderRequest(String buyerName,
                                     String buyerEmail,
                                     String shippingAddress,
                                     String notes,
                                     String paymentStatus,
                                     List<OrderLineRequest> items) {
    }

    public record OrderLineRequest(Long productId,
                                  String name,
                                  String slug,
                                  String unit,
                                  BigDecimal price,
                                  String currency,
                                  Integer quantity,
                                  String sellerName) {
    }

    public record OrderStatusUpdateRequest(String status, String paymentStatus) {
    }

    public record OrderResponse(Long id,
                                String buyerName,
                                String buyerEmail,
                                String shippingAddress,
                                String status,
                                String paymentStatus,
                                BigDecimal totalAmount,
                                Instant createdAt,
                                Integer itemCount) {
        public static OrderResponse from(Order order) {
            return new OrderResponse(
                    order.getId(),
                    order.getBuyerName(),
                    order.getBuyerEmail(),
                    order.getShippingAddress(),
                    order.getStatus().name(),
                    order.getPaymentStatus() != null ? order.getPaymentStatus().name() : null,
                    order.getTotalAmount(),
                    order.getCreatedAt(),
                    order.getItems() == null ? 0 : order.getItems().size());
        }
    }
}
