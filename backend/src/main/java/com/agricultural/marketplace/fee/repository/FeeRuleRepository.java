package com.agricultural.marketplace.fee.repository;

import java.util.List;
import java.util.Optional;

import com.agricultural.marketplace.fee.entity.FeeRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRuleRepository extends JpaRepository<FeeRule, Long> {

    List<FeeRule> findByActiveTrue();

    List<FeeRule> findByFeeTypeAndActiveTrue(String feeType);

    Optional<FeeRule> findFirstByFeeTypeAndActiveTrue(String feeType);
}

