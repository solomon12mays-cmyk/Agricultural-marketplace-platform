package com.agricultural.marketplace.product.repository;

import java.util.List;

import com.agricultural.marketplace.product.entity.GrainVariety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrainVarietyRepository extends JpaRepository<GrainVariety, Long> {

    List<GrainVariety> findByCropTypeIgnoreCase(String cropType);

    List<GrainVariety> findByRegionIgnoreCase(String region);
}

