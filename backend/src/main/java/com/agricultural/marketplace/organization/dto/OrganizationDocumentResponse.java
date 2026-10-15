package com.agricultural.marketplace.organization.dto;

import java.time.LocalDateTime;

import com.agricultural.marketplace.organization.entity.OrganizationDocument;

public record OrganizationDocumentResponse(
        Long id,
        Long organizationId,
        String documentType,
        String storageKey,
        String status,
        LocalDateTime uploadedAt
) {
    public static OrganizationDocumentResponse fromEntity(OrganizationDocument doc) {
        return new OrganizationDocumentResponse(
                doc.getId(),
                doc.getOrganizationId(),
                doc.getDocumentType(),
                doc.getStorageKey(),
                doc.getStatus(),
                doc.getUploadedAt()
        );
    }
}

