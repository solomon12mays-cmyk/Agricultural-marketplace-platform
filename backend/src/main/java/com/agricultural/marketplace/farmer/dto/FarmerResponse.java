package com.agricultural.marketplace.farmer.dto;

import java.time.LocalDateTime;

import com.agricultural.marketplace.farmer.entity.Farmer;

public record FarmerResponse(
        Long id,
        Long userId,
        String fullName,
        String nationalId,
        String phone,
        String region,
        String zone,
        String woreda,
        String verificationStatus,
        LocalDateTime verifiedAt,
        LocalDateTime createdAt) {

    public static FarmerResponse from(Farmer farmer) {
        return new FarmerResponse(
                farmer.getId(),
                farmer.getUserId(),
                farmer.getFullName(),
                farmer.getNationalId(),
                farmer.getPhone(),
                farmer.getRegion(),
                farmer.getZone(),
                farmer.getWoreda(),
                farmer.getVerificationStatus(),
                farmer.getVerifiedAt(),
                farmer.getCreatedAt());
    }
}

