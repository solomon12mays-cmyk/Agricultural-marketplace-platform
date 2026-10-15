package com.agricultural.marketplace.payment.repository;

import java.util.List;

import com.agricultural.marketplace.payment.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    List<PaymentTransaction> findByPaymentId(Long paymentId);
}

