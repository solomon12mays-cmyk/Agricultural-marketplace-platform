package com.agricultural.marketplace.personalbuyer.dto;

import java.time.LocalDateTime;

import com.agricultural.marketplace.personalbuyer.entity.PersonalBuyer;

public record PersonalBuyerResponse(
        Long id,
        Long userId,
        String fullName,
        String phone,
        String email,
        String region,
        String kycStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PersonalBuyerResponse fromEntity(PersonalBuyer pb) {
        return new PersonalBuyerResponse(
                pb.getId(),
                pb.getUserId(),
                pb.getFullName(),
                pb.getPhone(),
                pb.getEmail(),
                pb.getRegion(),
                pb.getKycStatus(),
                pb.getCreatedAt(),
                pb.getUpdatedAt()
        );
    }
}

