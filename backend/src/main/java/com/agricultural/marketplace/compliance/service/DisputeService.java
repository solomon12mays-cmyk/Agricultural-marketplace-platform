package com.agricultural.marketplace.compliance.service;

import java.util.List;

import com.agricultural.marketplace.compliance.dto.DisputeResponse;
import com.agricultural.marketplace.compliance.dto.OpenDisputeRequest;
import com.agricultural.marketplace.compliance.dto.ResolveDisputeRequest;
import com.agricultural.marketplace.compliance.entity.Dispute;
import com.agricultural.marketplace.compliance.repository.DisputeRepository;
import com.agricultural.marketplace.order.repository.OrderRepository;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DisputeService {

    private final DisputeRepository disputeRepository;
    private final OrderRepository orderRepository;

    public DisputeService(DisputeRepository disputeRepository, OrderRepository orderRepository) {
        this.disputeRepository = disputeRepository;
        this.orderRepository = orderRepository;
    }

    public DisputeResponse openDispute(OpenDisputeRequest request, String openedBy) {
        if (!orderRepository.existsById(request.orderId())) {
            throw new ResourceNotFoundException("Order not found with id: " + request.orderId());
        }

        Dispute dispute = new Dispute(
                request.orderId(),
                openedBy,
                request.respondentId(),
                "OPEN",
                request.reason()
        );
        Dispute saved = disputeRepository.save(dispute);
        return DisputeResponse.fromEntity(saved);
    }

    public DisputeResponse resolveDispute(Long id, ResolveDisputeRequest request) {
        Dispute dispute = disputeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispute not found with id: " + id));

        dispute.resolve(request.resolution(), request.status());
        return DisputeResponse.fromEntity(disputeRepository.save(dispute));
    }

    @Transactional(readOnly = true)
    public DisputeResponse getDisputeById(Long id) {
        Dispute dispute = disputeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispute not found with id: " + id));
        return DisputeResponse.fromEntity(dispute);
    }

    @Transactional(readOnly = true)
    public List<DisputeResponse> getDisputesByOrderId(Long orderId) {
        return disputeRepository.findByOrderId(orderId).stream()
                .map(DisputeResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<DisputeResponse> listDisputes(String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return disputeRepository.findByStatus(status.toUpperCase(), pageable)
                    .map(DisputeResponse::fromEntity);
        }
        return disputeRepository.findAll(pageable)
                .map(DisputeResponse::fromEntity);
    }
}
