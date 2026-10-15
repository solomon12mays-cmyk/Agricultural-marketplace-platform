package com.agricultural.marketplace.compliance.repository;

import java.util.List;

import com.agricultural.marketplace.compliance.entity.Dispute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute, Long> {

    List<Dispute> findByOrderId(Long orderId);

    List<Dispute> findByOpenedBy(String openedBy);

    Page<Dispute> findByStatus(String status, Pageable pageable);
}

