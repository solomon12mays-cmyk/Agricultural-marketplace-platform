package com.agricultural.marketplace.organization.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A registration or compliance document uploaded for an organization.
 */
@Entity
@Table(name = "organization_documents")
public class OrganizationDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "document_type", nullable = false, length = 64)
    private String documentType;

    @Column(name = "storage_key", nullable = false, length = 255)
    private String storageKey;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    protected OrganizationDocument() {
        // for JPA
    }

    public OrganizationDocument(Long organizationId, String documentType, String storageKey, String status) {
        this.organizationId = organizationId;
        this.documentType = documentType;
        this.storageKey = storageKey;
        this.status = status;
        this.uploadedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}
