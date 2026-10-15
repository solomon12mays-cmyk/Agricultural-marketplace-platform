package com.agricultural.marketplace.ledger.service;

import java.math.BigDecimal;
import java.util.List;

import com.agricultural.marketplace.ledger.entity.JournalEntry;
import com.agricultural.marketplace.ledger.entity.JournalLine;
import com.agricultural.marketplace.ledger.entity.LedgerAccount;
import com.agricultural.marketplace.ledger.repository.JournalEntryRepository;
import com.agricultural.marketplace.ledger.repository.JournalLineRepository;
import com.agricultural.marketplace.ledger.repository.LedgerAccountRepository;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reads balances and queries double-entry financial ledger accounts.
 */
@Service
@Transactional(readOnly = true)
public class LedgerService {

    private final LedgerAccountRepository accountRepository;
    private final JournalEntryRepository entryRepository;
    private final JournalLineRepository lineRepository;

    public LedgerService(LedgerAccountRepository accountRepository,
                         JournalEntryRepository entryRepository,
                         JournalLineRepository lineRepository) {
        this.accountRepository = accountRepository;
        this.entryRepository = entryRepository;
        this.lineRepository = lineRepository;
    }

    public List<LedgerAccount> listAccounts() {
        return accountRepository.findAll();
    }

    public BigDecimal balance(String accountCode) {
        LedgerAccount account = accountRepository.findByCode(accountCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ledger account not found with code: " + accountCode));

        List<JournalLine> lines = lineRepository.findAll().stream()
                .filter(l -> l.getAccountId().equals(account.getId()))
                .toList();

        BigDecimal debits = lines.stream()
                .filter(l -> "DEBIT".equalsIgnoreCase(l.getDirection()))
                .map(JournalLine::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal credits = lines.stream()
                .filter(l -> "CREDIT".equalsIgnoreCase(l.getDirection()))
                .map(JournalLine::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return debits.subtract(credits);
    }

    public List<JournalEntry> getEntriesByReference(String referenceType, String referenceId) {
        return entryRepository.findByReferenceTypeAndReferenceId(referenceType, referenceId);
    }

    public List<JournalLine> getLinesForEntry(Long entryId) {
        return lineRepository.findByEntryId(entryId);
    }
}
