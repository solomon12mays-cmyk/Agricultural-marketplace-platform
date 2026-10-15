package com.agricultural.marketplace.farmer.repository;

import java.util.Optional;

import com.agricultural.marketplace.farmer.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    Optional<Farmer> findByUserId(Long userId);

    Optional<Farmer> findByPhone(String phone);

    Optional<Farmer> findByNationalId(String nationalId);
}

