package com.agricultural.marketplace.ledger.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.agricultural.marketplace.ledger.entity.JournalEntry;
import com.agricultural.marketplace.ledger.entity.LedgerAccount;
import com.agricultural.marketplace.ledger.service.LedgerService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Double-entry ledger query endpoints.
 */
@RestController
@RequestMapping("/api/v1/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<ApiResponse<List<LedgerAccount>>> listAccounts() {
        return ResponseEntity.ok(ApiResponse.success(ledgerService.listAccounts()));
    }

    @GetMapping("/accounts/{code}/balance")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAccountBalance(@PathVariable String code) {
        BigDecimal balance = ledgerService.balance(code);
        return ResponseEntity.ok(ApiResponse.success(Map.of("accountCode", code, "netBalance", balance)));
    }

    @GetMapping("/entries")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getEntries(
            @RequestParam String referenceType,
            @RequestParam String referenceId) {
        return ResponseEntity.ok(ApiResponse.success(
                ledgerService.getEntriesByReference(referenceType, referenceId)));
    }
}

