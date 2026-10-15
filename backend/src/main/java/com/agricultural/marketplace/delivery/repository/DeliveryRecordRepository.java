package com.agricultural.marketplace.delivery.repository;

import java.util.List;
import java.util.Optional;

import com.agricultural.marketplace.delivery.entity.DeliveryRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRecordRepository extends JpaRepository<DeliveryRecord, Long> {

    Optional<DeliveryRecord> findByOrderId(Long orderId);

    List<DeliveryRecord> findByDriverId(Long driverId);

    Page<DeliveryRecord> findByStatus(String status, Pageable pageable);
}

