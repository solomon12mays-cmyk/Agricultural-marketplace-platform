package com.agricultural.marketplace.payout.repository;

import java.util.List;

import com.agricultural.marketplace.payout.entity.Payout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayoutRepository extends JpaRepository<Payout, Long> {

    List<Payout> findByPayeeTypeAndPayeeId(String payeeType, Long payeeId);

    List<Payout> findByStatus(String status);
}

