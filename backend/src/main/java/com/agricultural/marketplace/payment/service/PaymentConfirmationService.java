package com.agricultural.marketplace.payment.service;

import java.math.BigDecimal;

import com.agricultural.marketplace.order.entity.Order;
import com.agricultural.marketplace.order.repository.OrderRepository;
import com.agricultural.marketplace.payment.entity.Payment;
import com.agricultural.marketplace.payment.entity.PaymentTransaction;
import com.agricultural.marketplace.payment.repository.PaymentRepository;
import com.agricultural.marketplace.payment.repository.PaymentTransactionRepository;
import com.agricultural.marketplace.payment.state.PaymentStateMachine;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Confirms payments and advances order settlement.
 */
@Service
public class PaymentConfirmationService {

    private final PaymentRepository paymentRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final OrderRepository orderRepository;

    public PaymentConfirmationService(PaymentRepository paymentRepository,
                                      PaymentTransactionRepository transactionRepository,
                                      OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Payment confirm(Long paymentId, String gatewayReference, String method) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        PaymentStateMachine.Status current = PaymentStateMachine.Status.valueOf(payment.getStatus());
        PaymentStateMachine.assertTransition(current, PaymentStateMachine.Status.CAPTURED);

        payment.markPaid("GATEWAY", gatewayReference, method != null ? method : "CARD");
        Payment saved = paymentRepository.save(payment);

        transactionRepository.save(new PaymentTransaction(
                paymentId, "CAPTURE", payment.getAmount(), "SUCCESS"));

        // Advance Order payment status to PAID
        orderRepository.findById(payment.getOrderId()).ifPresent(order -> {
            order.setPaymentStatus("PAID");
            order.setStatus("CONFIRMED");
            orderRepository.save(order);
        });

        return saved;
    }
}
