package com.agricultural.marketplace.farmer.service;

import org.springframework.stereotype.Service;

/**
 * Verifies farmer identity against verification providers.
 *
 * <p>TODO: orchestrate identity and document verification (see the
 * verification module), update the farmer's verification status and record
 * an audit entry.</p>
 */
@Service
public class FarmerVerificationService {

    public void requestVerification(Long farmerId) {
        // TODO: start the verification workflow with the provider adapter.
        throw new UnsupportedOperationException("Farmer verification not implemented yet");
    }

    public void applyVerificationResult(Long farmerId, boolean approved, String notes) {
        // TODO: update the farmer status and notify the farmer.
        throw new UnsupportedOperationException("Farmer verification not implemented yet");
    }
}
