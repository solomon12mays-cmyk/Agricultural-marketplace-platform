package com.agricultural.marketplace.driver.repository;

import java.util.List;
import java.util.Optional;

import com.agricultural.marketplace.driver.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByDriverId(Long driverId);

    Optional<Vehicle> findByPlateNumber(String plateNumber);

    List<Vehicle> findByStatus(String status);
}

