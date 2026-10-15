package com.agricultural.marketplace.ledger.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * An account in the double-entry ledger (escrow, revenue, fee income,
 * farmer payable, driver payable, ...).
 */
@Entity
@Table(name = "ledger_accounts")
public class LedgerAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 64)
    private String code;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "account_type", nullable = false, length = 32)
    private String accountType;

    @Column(name = "owner_type", length = 32)
    private String ownerType;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected LedgerAccount() {
        // for JPA
    }

    public LedgerAccount(String code, String name, String accountType, String currency) {
        this.code = code;
        this.name = name;
        this.accountType = accountType;
        this.currency = currency;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
