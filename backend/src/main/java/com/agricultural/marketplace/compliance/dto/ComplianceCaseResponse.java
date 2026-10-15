package com.agricultural.marketplace.compliance.dto;

import java.time.LocalDateTime;

import com.agricultural.marketplace.compliance.entity.ComplianceCase;

public record ComplianceCaseResponse(
        Long id,
        String caseType,
        String status,
        String subjectType,
        String subjectId,
        String openedBy,
        String description,
        String resolution,
        LocalDateTime openedAt,
        LocalDateTime closedAt
) {
    public static ComplianceCaseResponse fromEntity(ComplianceCase c) {
        return new ComplianceCaseResponse(
                c.getId(),
                c.getCaseType(),
                c.getStatus(),
                c.getSubjectType(),
                c.getSubjectId(),
                c.getOpenedBy(),
                c.getDescription(),
                c.getResolution(),
                c.getOpenedAt(),
                c.getClosedAt()
        );
    }
}

