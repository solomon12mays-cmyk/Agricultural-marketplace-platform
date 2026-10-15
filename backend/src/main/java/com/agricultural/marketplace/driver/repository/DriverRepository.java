package com.agricultural.marketplace.driver.repository;

import java.util.List;
import java.util.Optional;

import com.agricultural.marketplace.driver.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUserId(Long userId);

    Optional<Driver> findByPhone(String phone);

    Optional<Driver> findByLicenseNumber(String licenseNumber);

    List<Driver> findByStatus(String status);
}

