package com.agricultural.marketplace.organization.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A cooperative, exporter or other organization doing business on the
 * platform.
 */
@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "tax_id", unique = true, length = 64)
    private String taxId;

    @Column(name = "registration_number", unique = true, length = 64)
    private String registrationNumber;

    @Column(name = "phone", length = 32)
    private String phone;

    @Column(name = "email", length = 128)
    private String email;

    @Column(name = "verification_status", nullable = false, length = 32)
    private String verificationStatus;

    @Column(name = "verification_notes", length = 500)
    private String verificationNotes;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Organization() {
        // for JPA
    }

    public Organization(String name, String verificationStatus) {
        this.name = name;
        this.verificationStatus = verificationStatus;
        this.createdAt = LocalDateTime.now();
    }

    public Organization(Long userId, String name, String taxId, String registrationNumber,
                        String phone, String email, String verificationStatus) {
        this.userId = userId;
        this.name = name;
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
        this.phone = phone;
        this.email = email;
        this.verificationStatus = verificationStatus;
        this.createdAt = LocalDateTime.now();
    }

    public void updateProfile(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateVerification(String status, String notes) {
        this.verificationStatus = status;
        this.verificationNotes = notes;
        if ("VERIFIED".equalsIgnoreCase(status) || "ACTIVE".equalsIgnoreCase(status)) {
            this.verifiedAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getTaxId() {
        return taxId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public String getVerificationNotes() {
        return verificationNotes;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
