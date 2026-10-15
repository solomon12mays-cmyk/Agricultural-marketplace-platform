package com.agricultural.marketplace.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reconciles recorded payments against gateway statements.
 *
 * <p>TODO: import gateway statements, detect missing/mismatched transactions
 * and raise {@code compliance} cases for unresolved differences.</p>
 */
@Service
public class PaymentReconciliationService {

    @Transactional
    public void reconcile(java.time.LocalDate statementDate) {
        // TODO: compare payments with the gateway statement for the day.
        throw new UnsupportedOperationException("Payment reconciliation not implemented yet");
    }
}
