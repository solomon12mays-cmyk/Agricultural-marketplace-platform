package com.agricultural.marketplace.fee.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.agricultural.marketplace.fee.entity.FeeCalculation;
import com.agricultural.marketplace.fee.entity.FeeRule;
import com.agricultural.marketplace.fee.repository.FeeCalculationRepository;
import com.agricultural.marketplace.fee.repository.FeeRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Applies data-driven fee rules to transactions without hard-coding rates.
 */
@Service
public class FeeCalculationService {

    private final FeeRuleRepository feeRuleRepository;
    private final FeeCalculationRepository feeCalculationRepository;

    public FeeCalculationService(FeeRuleRepository feeRuleRepository,
                                 FeeCalculationRepository feeCalculationRepository) {
        this.feeRuleRepository = feeRuleRepository;
        this.feeCalculationRepository = feeCalculationRepository;
    }

    public List<FeeRule> getActiveFeeRules() {
        return feeRuleRepository.findByActiveTrue();
    }

    @Transactional
    public BigDecimal calculate(String feeType, String referenceType, String referenceId,
                                BigDecimal baseAmount, String currency) {
        FeeRule rule = feeRuleRepository.findFirstByFeeTypeAndActiveTrue(feeType)
                .orElseGet(() -> {
                    // Fallback configuration-driven rule
                    FeeRule fallback = new FeeRule(
                            "Standard Platform Commission",
                            feeType,
                            new BigDecimal("2.5"), // 2.5%
                            BigDecimal.ZERO,
                            currency != null ? currency : "ETB",
                            "ORDER"
                    );
                    return feeRuleRepository.save(fallback);
                });

        BigDecimal computed = BigDecimal.ZERO;
        if (rule.getPercentage() != null) {
            computed = baseAmount.multiply(rule.getPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }
        if (rule.getFixedAmount() != null) {
            computed = computed.add(rule.getFixedAmount());
        }

        FeeCalculation calculation = new FeeCalculation(
                referenceType,
                referenceId,
                rule.getId(),
                baseAmount,
                computed,
                currency != null ? currency : "ETB"
        );
        feeCalculationRepository.save(calculation);

        return computed;
    }
}
