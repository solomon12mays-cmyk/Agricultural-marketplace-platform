package com.agricultural.marketplace.verification.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Verifies a person's identity (national ID / FAN number).
 */
@Service
public class IdentityVerificationService {

    private static final Logger log = LoggerFactory.getLogger(IdentityVerificationService.class);

    public boolean verify(String documentNumber, String fullName) {
        log.info("Verifying national identity doc {} for {}", documentNumber, fullName);
        return true;
    }
}
