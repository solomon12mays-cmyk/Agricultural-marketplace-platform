package com.agricultural.marketplace.personalbuyer.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateKycStatusRequest(
        @NotBlank(message = "KYC status is required (e.g. PENDING, VERIFIED, REJECTED)")
        String kycStatus
) {
}

