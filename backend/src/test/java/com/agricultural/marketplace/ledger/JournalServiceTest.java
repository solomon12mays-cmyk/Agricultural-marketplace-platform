package com.agricultural.marketplace.ledger;

import java.math.BigDecimal;
import java.util.List;

import com.agricultural.marketplace.ledger.repository.JournalEntryRepository;
import com.agricultural.marketplace.ledger.repository.JournalLineRepository;
import com.agricultural.marketplace.ledger.repository.LedgerAccountRepository;
import com.agricultural.marketplace.ledger.service.JournalService;
import com.agricultural.marketplace.shared.exception.BusinessRuleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JournalServiceTest {

    @Mock
    private LedgerAccountRepository ledgerAccountRepository;

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @Mock
    private JournalLineRepository journalLineRepository;

    private JournalService journalService;

    @BeforeEach
    void setUp() {
        journalService = new JournalService(ledgerAccountRepository, journalEntryRepository, journalLineRepository);
    }

    @Test
    @DisplayName("Unbalanced journal entry should be rejected with BusinessRuleException")
    void testUnbalancedJournalEntryRejected() {
        List<JournalService.JournalLineInput> lines = List.of(
                new JournalService.JournalLineInput("1010_CASH", "DEBIT", new BigDecimal("100.00"), "ETB"),
                new JournalService.JournalLineInput("2010_AP", "CREDIT", new BigDecimal("80.00"), "ETB")
        );

        assertThrows(BusinessRuleException.class, () ->
                journalService.post("ORDER", "ORD-1", "Unbalanced entry test", lines));
    }
}

