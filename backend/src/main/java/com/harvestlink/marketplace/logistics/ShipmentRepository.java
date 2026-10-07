package com.harvestlink.marketplace.logistics;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findAllByOrderByCreatedAtDesc();
}
