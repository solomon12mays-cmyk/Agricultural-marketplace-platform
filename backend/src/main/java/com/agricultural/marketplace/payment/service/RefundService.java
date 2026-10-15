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
 * Issues refunds and updates payment and order state.
 */
@Service
public class RefundService {

    private final PaymentRepository paymentRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final OrderRepository orderRepository;

    public RefundService(PaymentRepository paymentRepository,
                         PaymentTransactionRepository transactionRepository,
                         OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Payment refund(Long paymentId, BigDecimal amount, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        PaymentStateMachine.Status current = PaymentStateMachine.Status.valueOf(payment.getStatus());
        PaymentStateMachine.assertTransition(current, PaymentStateMachine.Status.REFUNDED);

        payment.setStatus("REFUNDED");
        Payment saved = paymentRepository.save(payment);

        BigDecimal refundAmt = amount != null ? amount : payment.getAmount();
        transactionRepository.save(new PaymentTransaction(
                paymentId, "REFUND", refundAmt, "SUCCESS"));

        orderRepository.findById(payment.getOrderId()).ifPresent(order -> {
            order.setPaymentStatus("REFUNDED");
            order.setStatus("REFUNDED");
            orderRepository.save(order);
        });

        return saved;
    }
}
