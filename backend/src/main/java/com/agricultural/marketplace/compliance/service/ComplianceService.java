package com.agricultural.marketplace.compliance.service;

import java.util.List;

import com.agricultural.marketplace.compliance.dto.ComplianceCaseResponse;
import com.agricultural.marketplace.compliance.dto.OpenComplianceCaseRequest;
import com.agricultural.marketplace.compliance.dto.ResolveComplianceCaseRequest;
import com.agricultural.marketplace.compliance.entity.ComplianceCase;
import com.agricultural.marketplace.compliance.repository.ComplianceCaseRepository;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ComplianceService {

    private final ComplianceCaseRepository complianceCaseRepository;

    public ComplianceService(ComplianceCaseRepository complianceCaseRepository) {
        this.complianceCaseRepository = complianceCaseRepository;
    }

    public ComplianceCaseResponse openCase(OpenComplianceCaseRequest request, String openedBy) {
        ComplianceCase cc = new ComplianceCase(
                request.caseType(),
                "OPEN",
                request.subjectType(),
                request.subjectId(),
                openedBy,
                request.description()
        );
        ComplianceCase saved = complianceCaseRepository.save(cc);
        return ComplianceCaseResponse.fromEntity(saved);
    }

    public ComplianceCaseResponse resolveCase(Long id, ResolveComplianceCaseRequest request) {
        ComplianceCase cc = complianceCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compliance case not found with id: " + id));

        cc.resolve(request.resolution(), request.status());
        return ComplianceCaseResponse.fromEntity(complianceCaseRepository.save(cc));
    }

    @Transactional(readOnly = true)
    public ComplianceCaseResponse getCaseById(Long id) {
        ComplianceCase cc = complianceCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compliance case not found with id: " + id));
        return ComplianceCaseResponse.fromEntity(cc);
    }

    @Transactional(readOnly = true)
    public Page<ComplianceCaseResponse> listCases(String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return complianceCaseRepository.findByStatus(status.toUpperCase(), pageable)
                    .map(ComplianceCaseResponse::fromEntity);
        }
        return complianceCaseRepository.findAll(pageable)
                .map(ComplianceCaseResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<ComplianceCaseResponse> listBySubject(String subjectType, String subjectId) {
        return complianceCaseRepository.findBySubjectTypeAndSubjectId(subjectType, subjectId).stream()
                .map(ComplianceCaseResponse::fromEntity)
                .toList();
    }
}

