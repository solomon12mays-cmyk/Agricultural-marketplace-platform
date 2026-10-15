package com.agricultural.marketplace.ledger.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.agricultural.marketplace.ledger.entity.JournalEntry;
import com.agricultural.marketplace.ledger.entity.JournalLine;
import com.agricultural.marketplace.ledger.entity.LedgerAccount;
import com.agricultural.marketplace.ledger.repository.JournalEntryRepository;
import com.agricultural.marketplace.ledger.repository.JournalLineRepository;
import com.agricultural.marketplace.ledger.repository.LedgerAccountRepository;
import com.agricultural.marketplace.shared.exception.BusinessRuleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Creates balanced double-entry journal postings.
 */
@Service
public class JournalService {

    public record JournalLineInput(String accountCode, String direction,
                                   BigDecimal amount, String currency) {
    }

    private final LedgerAccountRepository accountRepository;
    private final JournalEntryRepository entryRepository;
    private final JournalLineRepository lineRepository;

    public JournalService(LedgerAccountRepository accountRepository,
                          JournalEntryRepository entryRepository,
                          JournalLineRepository lineRepository) {
        this.accountRepository = accountRepository;
        this.entryRepository = entryRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Long post(String referenceType, String referenceId, String description,
                     List<JournalLineInput> lines) {
        if (lines == null || lines.size() < 2) {
            throw new BusinessRuleException("A double-entry posting requires at least two lines");
        }

        BigDecimal totalDebits = BigDecimal.ZERO;
        BigDecimal totalCredits = BigDecimal.ZERO;

        for (JournalLineInput line : lines) {
            if ("DEBIT".equalsIgnoreCase(line.direction())) {
                totalDebits = totalDebits.add(line.amount());
            } else if ("CREDIT".equalsIgnoreCase(line.direction())) {
                totalCredits = totalCredits.add(line.amount());
            } else {
                throw new BusinessRuleException("Invalid line direction: " + line.direction());
            }
        }

        // Invariant: Total Debits == Total Credits
        if (totalDebits.compareTo(totalCredits) != 0) {
            throw new BusinessRuleException(
                    "Double-entry journal out of balance! Debits: " + totalDebits + ", Credits: " + totalCredits);
        }

        JournalEntry entry = new JournalEntry(
                LocalDate.now(),
                referenceType,
                referenceId,
                description,
                "POSTED"
        );
        JournalEntry savedEntry = entryRepository.save(entry);

        for (JournalLineInput line : lines) {
            LedgerAccount account = accountRepository.findByCode(line.accountCode())
                    .orElseGet(() -> accountRepository.save(new LedgerAccount(
                            line.accountCode(),
                            line.accountCode().replace('_', ' '),
                            "GENERAL",
                            line.currency() != null ? line.currency() : "ETB"
                    )));

            JournalLine journalLine = new JournalLine(
                    savedEntry.getId(),
                    account.getId(),
                    line.direction().toUpperCase(),
                    line.amount(),
                    line.currency() != null ? line.currency() : "ETB"
            );
            lineRepository.save(journalLine);
        }

        return savedEntry.getId();
    }
}
