package com.agricultural.marketplace.organization.dto;

import java.time.LocalDateTime;

import com.agricultural.marketplace.organization.entity.Organization;

public record OrganizationResponse(
        Long id,
        Long userId,
        String name,
        String taxId,
        String registrationNumber,
        String phone,
        String email,
        String verificationStatus,
        String verificationNotes,
        LocalDateTime verifiedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static OrganizationResponse fromEntity(Organization org) {
        return new OrganizationResponse(
                org.getId(),
                org.getUserId(),
                org.getName(),
                org.getTaxId(),
                org.getRegistrationNumber(),
                org.getPhone(),
                org.getEmail(),
                org.getVerificationStatus(),
                org.getVerificationNotes(),
                org.getVerifiedAt(),
                org.getCreatedAt(),
                org.getUpdatedAt()
        );
    }
}

