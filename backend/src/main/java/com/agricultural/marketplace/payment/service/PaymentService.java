package com.agricultural.marketplace.payment.service;

import java.math.BigDecimal;
import java.util.List;

import com.agricultural.marketplace.order.entity.Order;
import com.agricultural.marketplace.order.repository.OrderRepository;
import com.agricultural.marketplace.payment.dto.CheckoutPaymentRequest;
import com.agricultural.marketplace.payment.dto.ConfirmPaymentRequest;
import com.agricultural.marketplace.payment.dto.PaymentResponse;
import com.agricultural.marketplace.payment.entity.Payment;
import com.agricultural.marketplace.payment.repository.PaymentRepository;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade over payment initiation, confirmation, refunds and reconciliation.
 */
@Service
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentConfirmationService confirmationService;
    private final RefundService refundService;

    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          PaymentConfirmationService confirmationService,
                          RefundService refundService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.confirmationService = confirmationService;
        this.refundService = refundService;
    }

    @Transactional
    public PaymentResponse initiatePayment(CheckoutPaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + request.orderId()));

        Payment payment = new Payment(
                order.getId(),
                request.payerType(),
                request.payerId(),
                order.getTotalAmount(),
                order.getCurrency(),
                "PENDING"
        );
        Payment saved = paymentRepository.save(payment);
        return PaymentResponse.from(saved);
    }

    @Transactional
    public PaymentResponse confirmPayment(Long paymentId, ConfirmPaymentRequest request) {
        Payment confirmed = confirmationService.confirm(
                paymentId, request.gatewayReference(), request.method());
        return PaymentResponse.from(confirmed);
    }

    @Transactional
    public PaymentResponse refund(Long paymentId, BigDecimal amount, String reason) {
        Payment refunded = refundService.refund(paymentId, amount, reason);
        return PaymentResponse.from(refunded);
    }

    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return PaymentResponse.from(payment);
    }

    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order id: " + orderId));
        return PaymentResponse.from(payment);
    }

    public List<PaymentResponse> getPaymentsByPayer(String payerType, Long payerId) {
        return paymentRepository.findByPayerTypeAndPayerId(payerType, payerId).stream()
                .map(PaymentResponse::from)
                .toList();
    }
}
