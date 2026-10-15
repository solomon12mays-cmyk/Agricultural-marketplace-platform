package com.agricultural.marketplace.order.controller;

import java.util.List;

import com.agricultural.marketplace.order.dto.CreateOrderRequest;
import com.agricultural.marketplace.order.dto.OrderResponse;
import com.agricultural.marketplace.order.dto.OrderStateHistoryResponse;
import com.agricultural.marketplace.order.service.OrderService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import com.agricultural.marketplace.shared.security.SecurityContextService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order endpoints: place, query, cancel and advance orders.
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final SecurityContextService securityContextService;

    public OrderController(OrderService orderService,
                           SecurityContextService securityContextService) {
        this.orderService = orderService;
        this.securityContextService = securityContextService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        String username = securityContextService.currentUsername().orElse("SYSTEM");
        OrderResponse response = orderService.placeOrder(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> listOrders(
            @RequestParam(required = false) String buyerType,
            @RequestParam(required = false) Long buyerId) {
        if (buyerType != null && buyerId != null) {
            return ResponseEntity.ok(ApiResponse.success(
                    orderService.getOrdersByBuyer(buyerType, buyerId)));
        }
        return ResponseEntity.ok(ApiResponse.success(orderService.getAllOrders()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrderById(id)));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        String username = securityContextService.currentUsername().orElse("SYSTEM");
        OrderResponse response = orderService.cancelOrder(id, username, reason);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{id}/transition")
    public ResponseEntity<ApiResponse<OrderResponse>> transitionOrder(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String reason) {
        String username = securityContextService.currentUsername().orElse("SYSTEM");
        OrderResponse response = orderService.transitionOrder(id, status, username, reason);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<OrderStateHistoryResponse>>> getOrderHistory(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrderHistory(id)));
    }
}
