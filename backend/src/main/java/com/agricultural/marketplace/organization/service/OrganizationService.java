package com.agricultural.marketplace.organization.service;

import java.util.List;
import java.util.Optional;

import com.agricultural.marketplace.organization.dto.CreateOrganizationRequest;
import com.agricultural.marketplace.organization.dto.OrganizationDocumentResponse;
import com.agricultural.marketplace.organization.dto.OrganizationResponse;
import com.agricultural.marketplace.organization.dto.OrganizationVerificationRequest;
import com.agricultural.marketplace.organization.dto.UpdateOrganizationRequest;
import com.agricultural.marketplace.organization.dto.UploadDocumentRequest;
import com.agricultural.marketplace.organization.entity.Organization;
import com.agricultural.marketplace.organization.entity.OrganizationDocument;
import com.agricultural.marketplace.organization.repository.OrganizationDocumentRepository;
import com.agricultural.marketplace.organization.repository.OrganizationRepository;
import com.agricultural.marketplace.organization.state.OrganizationVerificationStateMachine;
import com.agricultural.marketplace.shared.exception.ConflictException;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationDocumentRepository documentRepository;

    public OrganizationService(OrganizationRepository organizationRepository,
                               OrganizationDocumentRepository documentRepository) {
        this.organizationRepository = organizationRepository;
        this.documentRepository = documentRepository;
    }

    public OrganizationResponse createOrganization(CreateOrganizationRequest request) {
        if (request.taxId() != null && organizationRepository.findByTaxId(request.taxId()).isPresent()) {
            throw new ConflictException("Organization with Tax ID already exists: " + request.taxId());
        }
        if (request.registrationNumber() != null && organizationRepository.findByRegistrationNumber(request.registrationNumber()).isPresent()) {
            throw new ConflictException("Organization with Registration Number already exists: " + request.registrationNumber());
        }

        Organization org = new Organization(
                request.userId(),
                request.name(),
                request.taxId(),
                request.registrationNumber(),
                request.phone(),
                request.email(),
                "PENDING_VERIFICATION"
        );

        Organization saved = organizationRepository.save(org);
        return OrganizationResponse.fromEntity(saved);
    }

    public OrganizationResponse updateOrganization(Long id, UpdateOrganizationRequest request) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));

        org.updateProfile(request.name(), request.phone(), request.email());
        return OrganizationResponse.fromEntity(organizationRepository.save(org));
    }

    public OrganizationResponse updateVerificationStatus(Long id, OrganizationVerificationRequest request) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));

        OrganizationVerificationStateMachine.Status current =
                OrganizationVerificationStateMachine.Status.valueOf(org.getVerificationStatus());
        OrganizationVerificationStateMachine.Status target =
                OrganizationVerificationStateMachine.Status.valueOf(request.status().toUpperCase());

        OrganizationVerificationStateMachine.assertTransition(current, target);

        org.updateVerification(target.name(), request.notes());
        return OrganizationResponse.fromEntity(organizationRepository.save(org));
    }

    public OrganizationDocumentResponse uploadDocument(Long organizationId, UploadDocumentRequest request) {
        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException("Organization not found with id: " + organizationId);
        }

        OrganizationDocument document = new OrganizationDocument(
                organizationId,
                request.documentType(),
                request.storageKey(),
                "PENDING"
        );
        OrganizationDocument saved = documentRepository.save(document);
        return OrganizationDocumentResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<OrganizationDocumentResponse> getDocuments(Long organizationId) {
        return documentRepository.findByOrganizationId(organizationId).stream()
                .map(OrganizationDocumentResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrganizationResponse getById(Long id) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
        return OrganizationResponse.fromEntity(org);
    }

    @Transactional(readOnly = true)
    public Optional<OrganizationResponse> getByUserId(Long userId) {
        return organizationRepository.findByUserId(userId)
                .map(OrganizationResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<OrganizationResponse> listOrganizations(String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return organizationRepository.findByVerificationStatus(status.toUpperCase(), pageable)
                    .map(OrganizationResponse::fromEntity);
        }
        return organizationRepository.findAll(pageable)
                .map(OrganizationResponse::fromEntity);
    }
}
