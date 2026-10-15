package com.agricultural.marketplace.payment.repository;

import java.util.List;

import com.agricultural.marketplace.payment.entity.PaymentAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentAllocationRepository extends JpaRepository<PaymentAllocation, Long> {

    List<PaymentAllocation> findByPaymentId(Long paymentId);
}

