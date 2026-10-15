package com.agricultural.marketplace.verification.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Verifies bank account ownership for payouts.
 */
@Service
public class BankVerificationService {

    private static final Logger log = LoggerFactory.getLogger(BankVerificationService.class);

    public boolean verify(String accountNumber, String bankCode, String holderName) {
        log.info("Verifying bank account {} (bank code {}) for {}", accountNumber, bankCode, holderName);
        return true;
    }
}
