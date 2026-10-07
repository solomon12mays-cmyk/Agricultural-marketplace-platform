package com.harvestlink.marketplace.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_accounts", schema = "marketplace")
public class UserAccount implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true, length = 40)
    private String loginId;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "display_name", nullable = false, length = 120)
    private String displayName;

    @Column(name = "phone_number", unique = true, length = 40)
    private String phoneNumber;

    @Column(name = "fan_number", unique = true, length = 80)
    private String fanNumber;

    @Column(name = "farmer_id", unique = true)
    private Long farmerId;

    @Column(name = "organization_name", length = 120)
    private String organizationName;

    @Column(name = "contact_person_name", length = 120)
    private String contactPersonName;

    @Column(name = "tin_number", unique = true, length = 80)
    private String tinNumber;

    @Column(name = "license_number", unique = true, length = 80)
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_role", nullable = false, length = 20)
    private AccountRole role;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "must_change_password", nullable = false)
    private boolean mustChangePassword;

    protected UserAccount() {
    }

    public UserAccount(String loginId, String passwordHash, String displayName, String phoneNumber,
                       String fanNumber, Long farmerId, AccountRole role, boolean mustChangePassword) {
        this(loginId, passwordHash, displayName, phoneNumber, fanNumber, farmerId, role, mustChangePassword,
                null, null, null, null);
    }

    public UserAccount(String loginId, String passwordHash, String displayName, String phoneNumber,
                       String fanNumber, Long farmerId, AccountRole role, boolean mustChangePassword,
                       String organizationName, String contactPersonName, String tinNumber, String licenseNumber) {
        this.loginId = loginId;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.fanNumber = fanNumber;
        this.farmerId = farmerId;
        this.role = role;
        this.mustChangePassword = mustChangePassword;
        this.organizationName = organizationName;
        this.contactPersonName = contactPersonName;
        this.tinNumber = tinNumber;
        this.licenseNumber = licenseNumber;
    }

    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFanNumber() {
        return fanNumber;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public AccountRole getRole() {
        return role;
    }

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public void setMustChangePassword(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return loginId;
    }
}
