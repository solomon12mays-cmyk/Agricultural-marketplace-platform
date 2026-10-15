package com.agricultural.marketplace.farm.repository;

import java.util.Optional;

import com.agricultural.marketplace.farm.entity.FarmLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmLocationRepository extends JpaRepository<FarmLocation, Long> {

    Optional<FarmLocation> findByFarmId(Long farmId);
}

