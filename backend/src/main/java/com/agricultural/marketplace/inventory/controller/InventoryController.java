package com.agricultural.marketplace.inventory.controller;

import java.util.List;

import com.agricultural.marketplace.inventory.dto.AdjustStockRequest;
import com.agricultural.marketplace.inventory.dto.InventoryAccountResponse;
import com.agricultural.marketplace.inventory.dto.InventoryTransactionResponse;
import com.agricultural.marketplace.inventory.service.InventoryLedgerService;
import com.agricultural.marketplace.inventory.service.InventoryService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Inventory management and ledger history endpoints.
 */
@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryLedgerService ledgerService;

    public InventoryController(InventoryService inventoryService,
                               InventoryLedgerService ledgerService) {
        this.inventoryService = inventoryService;
        this.ledgerService = ledgerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryAccountResponse>>> listInventory(
            @RequestParam String ownerType,
            @RequestParam Long ownerId) {
        List<InventoryAccountResponse> accounts = inventoryService.getAccountsByOwner(ownerType, ownerId);
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryAccountResponse>> getInventory(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.getAccountById(id)));
    }

    @PostMapping("/adjust")
    public ResponseEntity<ApiResponse<InventoryAccountResponse>> adjustStock(
            @Valid @RequestBody AdjustStockRequest request) {
        InventoryAccountResponse response = inventoryService.adjust(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<ApiResponse<List<InventoryTransactionResponse>>> getTransactions(@PathVariable Long id) {
        List<InventoryTransactionResponse> transactions = ledgerService.statement(id);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }
}

