package com.agricultural.marketplace.personalbuyer.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Profile of an individual buyer.
 */
@Entity
@Table(name = "personal_buyers")
public class PersonalBuyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "full_name", nullable = false, length = 128)
    private String fullName;

    @Column(name = "phone", nullable = false, unique = true, length = 32)
    private String phone;

    @Column(name = "email", length = 128)
    private String email;

    @Column(name = "region", length = 64)
    private String region;

    @Column(name = "kyc_status", nullable = false, length = 32)
    private String kycStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected PersonalBuyer() {
        // for JPA
    }

    public PersonalBuyer(String fullName, String phone, String kycStatus) {
        this.fullName = fullName;
        this.phone = phone;
        this.kycStatus = kycStatus;
        this.createdAt = LocalDateTime.now();
    }

    public PersonalBuyer(Long userId, String fullName, String phone, String email, String region, String kycStatus) {
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.region = region;
        this.kycStatus = kycStatus;
        this.createdAt = LocalDateTime.now();
    }

    public void updateProfile(String fullName, String email, String region) {
        this.fullName = fullName;
        this.email = email;
        this.region = region;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
        this.updatedAt = LocalDateTime.now();
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

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getRegion() {
        return region;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
