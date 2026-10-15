package com.agricultural.marketplace.payout.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Computes and executes payouts owed to farmers.
 *
 * <p>TODO: sum settled sales minus fees/commissions, guard against duplicate
 * runs with a distributed lock, post the ledger entries and create the
 * {@code Payout} records.</p>
 */
@Service
public class FarmerPayoutService {

    @Transactional
    public void runPayoutCycle(java.time.LocalDate periodStart, java.time.LocalDate periodEnd) {
        // TODO: compute and execute farmer payouts for the period.
        throw new UnsupportedOperationException("Farmer payouts not implemented yet");
    }
}
