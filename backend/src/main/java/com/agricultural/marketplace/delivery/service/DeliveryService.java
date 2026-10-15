package com.agricultural.marketplace.delivery.service;

import com.agricultural.marketplace.delivery.dto.ConfirmDeliveryRequest;
import com.agricultural.marketplace.delivery.dto.DeliveryResponse;
import com.agricultural.marketplace.delivery.entity.DeliveryRecord;
import com.agricultural.marketplace.delivery.repository.DeliveryRecordRepository;
import com.agricultural.marketplace.order.entity.Order;
import com.agricultural.marketplace.order.repository.OrderRepository;
import com.agricultural.marketplace.order.service.OrderService;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeliveryService {

    private final DeliveryRecordRepository deliveryRecordRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public DeliveryService(DeliveryRecordRepository deliveryRecordRepository,
                           OrderRepository orderRepository,
                           OrderService orderService) {
        this.deliveryRecordRepository = deliveryRecordRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    public DeliveryResponse recordDelivery(Long orderId, ConfirmDeliveryRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        DeliveryRecord record = deliveryRecordRepository.findByOrderId(orderId)
                .orElseGet(() -> new DeliveryRecord(orderId, request.assignmentId(), request.driverId(), "PENDING"));

        record.markDelivered(request.receivedBy(), request.proofNote(), request.conditionNote());
        DeliveryRecord saved = deliveryRecordRepository.save(record);

        // Transition order lifecycle
        if ("IN_TRANSIT".equalsIgnoreCase(order.getStatus())) {
            orderService.transitionOrder(orderId, "DELIVERED", request.receivedBy(), request.proofNote());
            orderService.transitionOrder(orderId, "COMPLETED", request.receivedBy(), "Goods delivered and accepted");
        } else if ("DELIVERED".equalsIgnoreCase(order.getStatus())) {
            orderService.transitionOrder(orderId, "COMPLETED", request.receivedBy(), "Goods delivery finalized");
        }

        return DeliveryResponse.fromEntity(saved);
    }

    public void confirmDelivery(Long orderId, String receivedBy, String proofNote) {
        ConfirmDeliveryRequest request = new ConfirmDeliveryRequest(
                null,
                null,
                receivedBy,
                proofNote,
                "Good condition"
        );
        recordDelivery(orderId, request);
    }

    public void reportFailure(Long orderId, String reason) {
        DeliveryRecord record = deliveryRecordRepository.findByOrderId(orderId)
                .orElseGet(() -> new DeliveryRecord(orderId, "FAILED"));

        record.markFailed(reason);
        deliveryRecordRepository.save(record);
    }

    @Transactional(readOnly = true)
    public DeliveryResponse getDeliveryById(Long id) {
        DeliveryRecord record = deliveryRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery record not found: " + id));
        return DeliveryResponse.fromEntity(record);
    }

    @Transactional(readOnly = true)
    public DeliveryResponse getDeliveryByOrderId(Long orderId) {
        DeliveryRecord record = deliveryRecordRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery record not found for order: " + orderId));
        return DeliveryResponse.fromEntity(record);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryResponse> listDeliveries(String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return deliveryRecordRepository.findByStatus(status, pageable)
                    .map(DeliveryResponse::fromEntity);
        }
        return deliveryRecordRepository.findAll(pageable)
                .map(DeliveryResponse::fromEntity);
    }
}
