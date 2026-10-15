package com.agricultural.marketplace.payout.service;

import java.math.BigDecimal;
import java.util.List;

import com.agricultural.marketplace.payout.entity.Payout;
import com.agricultural.marketplace.payout.repository.PayoutRepository;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade over farmer and driver payout generation and reconciliation.
 */
@Service
@Transactional(readOnly = true)
public class PayoutService {

    private final PayoutRepository payoutRepository;
    private final FarmerPayoutService farmerPayoutService;
    private final DriverPayoutService driverPayoutService;
    private final PayoutReconciliationService reconciliationService;

    public PayoutService(PayoutRepository payoutRepository,
                         FarmerPayoutService farmerPayoutService,
                         DriverPayoutService driverPayoutService,
                         PayoutReconciliationService reconciliationService) {
        this.payoutRepository = payoutRepository;
        this.farmerPayoutService = farmerPayoutService;
        this.driverPayoutService = driverPayoutService;
        this.reconciliationService = reconciliationService;
    }

    public List<Payout> getAllPayouts() {
        return payoutRepository.findAll();
    }

    public List<Payout> getPayoutsByPayee(String payeeType, Long payeeId) {
        return payoutRepository.findByPayeeTypeAndPayeeId(payeeType, payeeId);
    }

    public Payout getPayoutById(Long id) {
        return payoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payout not found with id: " + id));
    }

    @Transactional
    public Payout createPayout(String payeeType, Long payeeId, BigDecimal amount, String currency) {
        Payout payout = new Payout(
                payeeType,
                payeeId,
                amount,
                currency != null ? currency : "ETB",
                "PENDING"
        );
        return payoutRepository.save(payout);
    }

    @Transactional
    public Payout processPayout(Long payoutId, String reference) {
        Payout payout = getPayoutById(payoutId);
        payout.markProcessed(reference != null ? reference : "BANK-REF-" + System.currentTimeMillis());
        return payoutRepository.save(payout);
    }
}
