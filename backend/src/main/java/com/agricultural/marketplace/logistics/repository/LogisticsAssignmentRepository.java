package com.agricultural.marketplace.logistics.repository;

import java.util.List;
import java.util.Optional;

import com.agricultural.marketplace.logistics.entity.LogisticsAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticsAssignmentRepository extends JpaRepository<LogisticsAssignment, Long> {

    Optional<LogisticsAssignment> findByOrderId(Long orderId);

    List<LogisticsAssignment> findByDriverId(Long driverId);

    Page<LogisticsAssignment> findByStatus(String status, Pageable pageable);
}

