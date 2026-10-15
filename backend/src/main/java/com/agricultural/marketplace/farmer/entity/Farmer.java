package com.agricultural.marketplace.farmer.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Profile of a farmer account.
 */
@Entity
@Table(name = "farmers")
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "full_name", nullable = false, length = 128)
    private String fullName;

    @Column(name = "national_id", nullable = false, unique = true, length = 64)
    private String nationalId;

    @Column(name = "phone", nullable = false, unique = true, length = 32)
    private String phone;

    @Column(name = "region", length = 64)
    private String region;

    @Column(name = "zone", length = 64)
    private String zone;

    @Column(name = "woreda", length = 64)
    private String woreda;

    @Column(name = "verification_status", nullable = false, length = 32)
    private String verificationStatus;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Farmer() {
        // for JPA
    }

    public Farmer(String fullName, String nationalId, String phone, String verificationStatus) {
        this(null, fullName, nationalId, phone, null, null, null, verificationStatus);
    }

    public Farmer(Long userId, String fullName, String nationalId, String phone,
                  String region, String zone, String woreda, String verificationStatus) {
        this.userId = userId;
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.phone = phone;
        this.region = region;
        this.zone = zone;
        this.woreda = woreda;
        this.verificationStatus = verificationStatus;
        this.createdAt = LocalDateTime.now();
    }

    public void updateProfile(String fullName, String region, String zone, String woreda) {
        if (fullName != null && !fullName.isBlank()) {
            this.fullName = fullName;
        }
        this.region = region;
        this.zone = zone;
        this.woreda = woreda;
        this.updatedAt = LocalDateTime.now();
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
        if ("VERIFIED".equalsIgnoreCase(verificationStatus)) {
            this.verifiedAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getPhone() {
        return phone;
    }

    public String getRegion() {
        return region;
    }

    public String getZone() {
        return zone;
    }

    public String getWoreda() {
        return woreda;
    }

    public String getVerificationStatus() {
        return verificationStatus;
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
