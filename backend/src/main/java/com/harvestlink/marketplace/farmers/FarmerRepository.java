package com.harvestlink.marketplace.farmers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    List<Farmer> findAllByOrderByNameAsc();
    Optional<Farmer> findBySlugIgnoreCase(String slug);
}
