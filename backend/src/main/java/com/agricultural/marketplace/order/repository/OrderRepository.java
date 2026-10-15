package com.agricultural.marketplace.order.repository;

import java.util.List;
import java.util.Optional;

import com.agricultural.marketplace.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByBuyerTypeAndBuyerId(String buyerType, Long buyerId);

    List<Order> findByStatus(String status);
}

