package com.agricultural.marketplace.order.service;

import java.util.List;

import com.agricultural.marketplace.order.dto.CreateOrderRequest;
import com.agricultural.marketplace.order.dto.OrderItemResponse;
import com.agricultural.marketplace.order.dto.OrderResponse;
import com.agricultural.marketplace.order.dto.OrderStateHistoryResponse;
import com.agricultural.marketplace.order.entity.Order;
import com.agricultural.marketplace.order.entity.OrderItem;
import com.agricultural.marketplace.order.entity.OrderStateHistory;
import com.agricultural.marketplace.order.repository.OrderItemRepository;
import com.agricultural.marketplace.order.repository.OrderRepository;
import com.agricultural.marketplace.order.repository.OrderStateHistoryRepository;
import com.agricultural.marketplace.order.state.OrderStateMachine;
import com.agricultural.marketplace.product.entity.Product;
import com.agricultural.marketplace.product.repository.ProductRepository;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade over order placement, reservation and lifecycle transitions.
 */
@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderCreationService creationService;
    private final OrderReservationService reservationService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStateHistoryRepository stateHistoryRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderCreationService creationService,
                        OrderReservationService reservationService,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        OrderStateHistoryRepository stateHistoryRepository,
                        ProductRepository productRepository) {
        this.creationService = creationService;
        this.reservationService = reservationService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.stateHistoryRepository = stateHistoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse placeOrder(CreateOrderRequest request, String createdBy) {
        Long orderId = creationService.createOrder(request, createdBy);
        return getOrderById(orderId);
    }

    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return toOrderResponse(order);
    }

    public List<OrderResponse> getOrdersByBuyer(String buyerType, Long buyerId) {
        return orderRepository.findByBuyerTypeAndBuyerId(buyerType, buyerId).stream()
                .map(this::toOrderResponse)
                .toList();
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toOrderResponse)
                .toList();
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId, String changedBy, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        OrderStateMachine.Status current = OrderStateMachine.Status.valueOf(order.getStatus());
        OrderStateMachine.assertTransition(current, OrderStateMachine.Status.CANCELLED);

        reservationService.releaseStock(orderId);

        order.setStatus("CANCELLED");
        Order saved = orderRepository.save(order);

        stateHistoryRepository.save(new OrderStateHistory(
                orderId,
                current.name(),
                "CANCELLED",
                changedBy,
                reason != null ? reason : "Order cancelled by buyer/system"
        ));

        return toOrderResponse(saved);
    }

    @Transactional
    public OrderResponse transitionOrder(Long orderId, String targetStatus, String changedBy, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        OrderStateMachine.Status current = OrderStateMachine.Status.valueOf(order.getStatus());
        OrderStateMachine.Status target = OrderStateMachine.Status.valueOf(targetStatus.toUpperCase());
        OrderStateMachine.assertTransition(current, target);

        // When order moves to COMPLETED or PICKED_UP, consume reserved stock
        if (target == OrderStateMachine.Status.COMPLETED) {
            reservationService.consumeStock(orderId);
        } else if (target == OrderStateMachine.Status.CANCELLED || target == OrderStateMachine.Status.REFUNDED) {
            reservationService.releaseStock(orderId);
        }

        order.setStatus(target.name());
        Order saved = orderRepository.save(order);

        stateHistoryRepository.save(new OrderStateHistory(
                orderId,
                current.name(),
                target.name(),
                changedBy,
                reason != null ? reason : "State advanced to " + target.name()
        ));

        return toOrderResponse(saved);
    }

    public List<OrderStateHistoryResponse> getOrderHistory(Long orderId) {
        return stateHistoryRepository.findByOrderIdOrderByChangedAtAsc(orderId).stream()
                .map(OrderStateHistoryResponse::from)
                .toList();
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        List<OrderItemResponse> itemResponses = items.stream()
                .map(item -> {
                    String productName = productRepository.findById(item.getProductId())
                            .map(Product::getName)
                            .orElse("Product");
                    return OrderItemResponse.of(item, productName);
                })
                .toList();
        return OrderResponse.of(order, itemResponses);
    }
}
