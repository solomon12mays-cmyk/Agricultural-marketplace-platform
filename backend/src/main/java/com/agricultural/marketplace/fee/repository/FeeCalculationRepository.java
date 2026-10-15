package com.agricultural.marketplace.fee.repository;

import java.util.List;

import com.agricultural.marketplace.fee.entity.FeeCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeCalculationRepository extends JpaRepository<FeeCalculation, Long> {

    List<FeeCalculation> findByReferenceTypeAndReferenceId(String referenceType, String referenceId);
}

