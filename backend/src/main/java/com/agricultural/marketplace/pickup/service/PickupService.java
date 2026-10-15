package com.agricultural.marketplace.pickup.service;

import java.time.LocalDateTime;

import com.agricultural.marketplace.order.entity.Order;
import com.agricultural.marketplace.order.repository.OrderRepository;
import com.agricultural.marketplace.order.service.OrderService;
import com.agricultural.marketplace.pickup.dto.PickupResponse;
import com.agricultural.marketplace.pickup.dto.SchedulePickupRequest;
import com.agricultural.marketplace.pickup.entity.PickupRecord;
import com.agricultural.marketplace.pickup.repository.PickupRecordRepository;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PickupService {

    private final PickupRecordRepository pickupRecordRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public PickupService(PickupRecordRepository pickupRecordRepository,
                         OrderRepository orderRepository,
                         OrderService orderService) {
        this.pickupRecordRepository = pickupRecordRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    public Long schedule(Long orderId, LocalDateTime scheduledAt) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        PickupRecord record = pickupRecordRepository.findByOrderId(orderId)
                .orElseGet(() -> new PickupRecord(orderId, "SCHEDULED"));

        PickupRecord toSave = new PickupRecord(
                orderId,
                null,
                null,
                scheduledAt,
                "Scheduled for pickup",
                "SCHEDULED"
        );
        return pickupRecordRepository.save(toSave).getId();
    }

    public PickupResponse schedulePickup(SchedulePickupRequest request) {
        if (!orderRepository.existsById(request.orderId())) {
            throw new ResourceNotFoundException("Order not found: " + request.orderId());
        }

        PickupRecord record = new PickupRecord(
                request.orderId(),
                request.assignmentId(),
                request.farmerId(),
                request.scheduledAt(),
                request.locationNote(),
                "SCHEDULED"
        );
        PickupRecord saved = pickupRecordRepository.save(record);
        return PickupResponse.fromEntity(saved);
    }

    public PickupResponse confirmPickup(Long pickupId, String receivedBy) {
        PickupRecord record = pickupRecordRepository.findById(pickupId)
                .orElseThrow(() -> new ResourceNotFoundException("Pickup record not found: " + pickupId));

        record.markPickedUp(receivedBy);
        PickupRecord saved = pickupRecordRepository.save(record);

        // Advance the order status if possible
        Order order = orderRepository.findById(record.getOrderId()).orElse(null);
        if (order != null) {
            if ("PAID".equalsIgnoreCase(order.getStatus())) {
                orderService.transitionOrder(order.getId(), "PICKING", receivedBy, "Driver arrived for pickup");
                orderService.transitionOrder(order.getId(), "IN_TRANSIT", receivedBy, "Goods picked up from farm");
            } else if ("PICKING".equalsIgnoreCase(order.getStatus())) {
                orderService.transitionOrder(order.getId(), "IN_TRANSIT", receivedBy, "Goods picked up from farm");
            }
        }

        return PickupResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public PickupResponse getPickupById(Long id) {
        PickupRecord record = pickupRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pickup record not found: " + id));
        return PickupResponse.fromEntity(record);
    }

    @Transactional(readOnly = true)
    public PickupResponse getPickupByOrderId(Long orderId) {
        PickupRecord record = pickupRecordRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pickup record not found for order: " + orderId));
        return PickupResponse.fromEntity(record);
    }

    @Transactional(readOnly = true)
    public Page<PickupResponse> listPickups(String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return pickupRecordRepository.findByStatus(status, pageable)
                    .map(PickupResponse::fromEntity);
        }
        return pickupRecordRepository.findAll(pageable)
                .map(PickupResponse::fromEntity);
    }
}
