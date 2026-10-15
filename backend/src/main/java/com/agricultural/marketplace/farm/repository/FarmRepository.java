package com.agricultural.marketplace.farm.repository;

import java.util.List;

import com.agricultural.marketplace.farm.entity.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> {

    List<Farm> findByFarmerId(Long farmerId);
}

