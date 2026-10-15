package com.agricultural.marketplace.order.repository;

import java.util.List;

import com.agricultural.marketplace.order.entity.OrderStateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStateHistoryRepository extends JpaRepository<OrderStateHistory, Long> {

    List<OrderStateHistory> findByOrderIdOrderByChangedAtAsc(Long orderId);
}

