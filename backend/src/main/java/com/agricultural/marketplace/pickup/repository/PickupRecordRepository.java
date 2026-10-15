package com.agricultural.marketplace.pickup.repository;

import java.util.List;
import java.util.Optional;

import com.agricultural.marketplace.pickup.entity.PickupRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickupRecordRepository extends JpaRepository<PickupRecord, Long> {

    Optional<PickupRecord> findByOrderId(Long orderId);

    List<PickupRecord> findByFarmerId(Long farmerId);

    Page<PickupRecord> findByStatus(String status, Pageable pageable);
}

