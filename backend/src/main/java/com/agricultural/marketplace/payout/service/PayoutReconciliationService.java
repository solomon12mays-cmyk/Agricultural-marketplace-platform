package com.agricultural.marketplace.payout.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reconciles payout records against bank/wallet statements and the ledger.
 *
 * <p>TODO: match processed payouts with statements, detect failed or missing
 * transfers and trigger retries or compliance cases.</p>
 */
@Service
public class PayoutReconciliationService {

    @Transactional
    public void reconcile(LocalDate statementDate) {
        // TODO: compare payouts with the disbursement statement and ledger.
        throw new UnsupportedOperationException("Payout reconciliation not implemented yet");
    }
}
