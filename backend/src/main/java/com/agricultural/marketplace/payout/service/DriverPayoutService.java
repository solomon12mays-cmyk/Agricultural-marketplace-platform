package com.agricultural.marketplace.payout.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Computes and executes payouts owed to drivers for completed deliveries.
 *
 * <p>TODO: sum delivery earnings, guard against duplicate runs, post ledger
 * entries and create the {@code Payout} records.</p>
 */
@Service
public class DriverPayoutService {

    @Transactional
    public void runPayoutCycle(java.time.LocalDate periodStart, java.time.LocalDate periodEnd) {
        // TODO: compute and execute driver payouts for the period.
        throw new UnsupportedOperationException("Driver payouts not implemented yet");
    }
}
