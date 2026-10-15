package com.agricultural.marketplace.payment.repository;

import java.util.List;
import java.util.Optional;

import com.agricultural.marketplace.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    List<Payment> findByPayerTypeAndPayerId(String payerType, Long payerId);

    List<Payment> findByStatus(String status);
}

