package com.agricultural.marketplace.ledger.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A balanced set of journal lines recording a financial event.
 */
@Entity
@Table(name = "journal_entries")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "reference_type", nullable = false, length = 32)
    private String referenceType;

    @Column(name = "reference_id", nullable = false, length = 64)
    private String referenceId;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected JournalEntry() {
        // for JPA
    }

    public JournalEntry(LocalDate entryDate, String referenceType, String referenceId,
                        String description, String status) {
        this.entryDate = entryDate;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.description = description;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
