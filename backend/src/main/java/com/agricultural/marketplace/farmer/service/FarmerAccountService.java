package com.agricultural.marketplace.farmer.service;

import org.springframework.stereotype.Service;

/**
 * Manages the link between farmer profiles and platform accounts.
 *
 * <p>TODO: activate/suspend accounts, enforce password change on first
 * sign-in and revoke sessions when a profile is suspended.</p>
 */
@Service
public class FarmerAccountService {

    public void suspend(Long farmerId, String reason) {
        // TODO: suspend the linked user account and revoke its sessions.
        throw new UnsupportedOperationException("Farmer account management not implemented yet");
    }

    public void activate(Long farmerId) {
        // TODO: reactivate the linked user account.
        throw new UnsupportedOperationException("Farmer account management not implemented yet");
    }
}
