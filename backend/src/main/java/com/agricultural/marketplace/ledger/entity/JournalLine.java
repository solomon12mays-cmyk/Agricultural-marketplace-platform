package com.agricultural.marketplace.ledger.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * One debit or credit line of a journal entry.
 */
@Entity
@Table(name = "journal_lines")
public class JournalLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entry_id", nullable = false)
    private Long entryId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "direction", nullable = false, length = 8)
    private String direction;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    protected JournalLine() {
        // for JPA
    }

    public JournalLine(Long entryId, Long accountId, String direction,
                       BigDecimal amount, String currency) {
        this.entryId = entryId;
        this.accountId = accountId;
        this.direction = direction;
        this.amount = amount;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public Long getEntryId() {
        return entryId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getDirection() {
        return direction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
