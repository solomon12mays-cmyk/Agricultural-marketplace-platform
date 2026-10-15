package com.agricultural.marketplace.compliance.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A compliance review case opened against an entity or transaction.
 */
@Entity
@Table(name = "compliance_cases")
public class ComplianceCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_type", nullable = false, length = 64)
    private String caseType;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "subject_type", nullable = false, length = 32)
    private String subjectType;

    @Column(name = "subject_id", nullable = false, length = 64)
    private String subjectId;

    @Column(name = "opened_by", length = 64)
    private String openedBy;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "resolution", length = 2000)
    private String resolution;

    @Column(name = "opened_at", nullable = false)
    private LocalDateTime openedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    protected ComplianceCase() {
        // for JPA
    }

    public ComplianceCase(String caseType, String status, String subjectType, String subjectId) {
        this.caseType = caseType;
        this.status = status;
        this.subjectType = subjectType;
        this.subjectId = subjectId;
        this.openedAt = LocalDateTime.now();
    }

    public ComplianceCase(String caseType, String status, String subjectType, String subjectId,
                          String openedBy, String description) {
        this.caseType = caseType;
        this.status = status;
        this.subjectType = subjectType;
        this.subjectId = subjectId;
        this.openedBy = openedBy;
        this.description = description;
        this.openedAt = LocalDateTime.now();
    }

    public void resolve(String resolution, String status) {
        this.resolution = resolution;
        this.status = status != null ? status : "RESOLVED";
        this.closedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getCaseType() {
        return caseType;
    }

    public String getStatus() {
        return status;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getOpenedBy() {
        return openedBy;
    }

    public String getDescription() {
        return description;
    }

    public String getResolution() {
        return resolution;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }
}
