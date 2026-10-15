package com.agricultural.marketplace.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.agricultural.marketplace.listing.entity.Listing;
import com.agricultural.marketplace.listing.repository.ListingRepository;
import com.agricultural.marketplace.order.dto.CreateOrderItemRequest;
import com.agricultural.marketplace.order.dto.CreateOrderRequest;
import com.agricultural.marketplace.order.entity.Order;
import com.agricultural.marketplace.order.entity.OrderItem;
import com.agricultural.marketplace.order.entity.OrderStateHistory;
import com.agricultural.marketplace.order.repository.OrderItemRepository;
import com.agricultural.marketplace.order.repository.OrderRepository;
import com.agricultural.marketplace.order.repository.OrderStateHistoryRepository;
import com.agricultural.marketplace.shared.exception.BusinessRuleException;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Creates orders atomically: validates lines, computes totals and reserves stock.
 */
@Service
public class OrderCreationService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStateHistoryRepository stateHistoryRepository;
    private final ListingRepository listingRepository;
    private final OrderReservationService orderReservationService;

    public OrderCreationService(OrderRepository orderRepository,
                                OrderItemRepository orderItemRepository,
                                OrderStateHistoryRepository stateHistoryRepository,
                                ListingRepository listingRepository,
                                OrderReservationService orderReservationService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.stateHistoryRepository = stateHistoryRepository;
        this.listingRepository = listingRepository;
        this.orderReservationService = orderReservationService;
    }

    @Transactional
    public Long createOrder(CreateOrderRequest request, String createdBy) {
        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        BigDecimal totalAmount = BigDecimal.ZERO;
        String currency = "ETB";

        List<OrderItem> itemsToSave = new ArrayList<>();

        for (CreateOrderItemRequest lineRequest : request.items()) {
            Listing listing = listingRepository.findById(lineRequest.listingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Listing not found: " + lineRequest.listingId()));

            if (!"ACTIVE".equalsIgnoreCase(listing.getStatus())) {
                throw new BusinessRuleException("Listing is not active: " + listing.getId());
            }

            if (listing.getQuantityAvailable().compareTo(lineRequest.quantity()) < 0) {
                throw new BusinessRuleException("Requested quantity (" + lineRequest.quantity()
                        + ") exceeds listing available quantity (" + listing.getQuantityAvailable() + ")");
            }

            currency = listing.getCurrency();
            BigDecimal lineTotal = listing.getPricePerUnit().multiply(lineRequest.quantity());
            totalAmount = totalAmount.add(lineTotal);

            // Temporarily set orderId to 0, will update after order is persisted
            OrderItem item = new OrderItem(
                    0L,
                    listing.getId(),
                    listing.getProductId(),
                    lineRequest.quantity(),
                    listing.getUnit(),
                    listing.getPricePerUnit(),
                    lineTotal
            );
            itemsToSave.add(item);
        }

        Order order = new Order(
                orderNumber,
                request.buyerType(),
                request.buyerId(),
                totalAmount,
                currency
        );
        order.setDeliveryInfo(request.deliveryRegion(), request.deliveryAddress(), request.notes());
        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : itemsToSave) {
            OrderItem withOrderId = new OrderItem(
                    savedOrder.getId(),
                    item.getListingId(),
                    item.getProductId(),
                    item.getQuantity(),
                    item.getUnit(),
                    item.getUnitPrice(),
                    item.getLineTotal()
            );
            orderItemRepository.save(withOrderId);
        }

        // Reserve stock
        orderReservationService.reserveStock(savedOrder.getId());

        // Record initial state history
        stateHistoryRepository.save(new OrderStateHistory(
                savedOrder.getId(),
                null,
                "PENDING",
                createdBy,
                "Order created and stock soft-reserved"
        ));

        return savedOrder.getId();
    }
}
