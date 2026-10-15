package com.agricultural.marketplace.inventory.service;

import java.math.BigDecimal;
import java.util.List;

import com.agricultural.marketplace.inventory.dto.AdjustStockRequest;
import com.agricultural.marketplace.inventory.dto.InventoryAccountResponse;
import com.agricultural.marketplace.inventory.entity.InventoryAccount;
import com.agricultural.marketplace.inventory.repository.InventoryAccountRepository;
import com.agricultural.marketplace.shared.exception.BusinessRuleException;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InventoryService {

    private final InventoryAccountRepository accountRepository;
    private final InventoryLedgerService ledgerService;

    public InventoryService(InventoryAccountRepository accountRepository,
                            InventoryLedgerService ledgerService) {
        this.accountRepository = accountRepository;
        this.ledgerService = ledgerService;
    }

    public BigDecimal availableQuantity(String ownerType, Long ownerId, Long productId) {
        return accountRepository.findByOwnerTypeAndOwnerIdAndProductId(ownerType, ownerId, productId)
                .map(InventoryAccount::getAvailableQuantity)
                .orElse(BigDecimal.ZERO);
    }

    public List<InventoryAccountResponse> getAccountsByOwner(String ownerType, Long ownerId) {
        return accountRepository.findByOwnerTypeAndOwnerId(ownerType, ownerId).stream()
                .map(InventoryAccountResponse::from)
                .toList();
    }

    public InventoryAccountResponse getAccountById(Long id) {
        InventoryAccount account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory account not found with id: " + id));
        return InventoryAccountResponse.from(account);
    }

    @Transactional
    public InventoryAccountResponse adjust(AdjustStockRequest request) {
        InventoryAccount account = accountRepository.findByOwnerTypeAndOwnerIdAndProductId(
                request.ownerType(), request.ownerId(), request.productId())
                .orElseGet(() -> {
                    String unit = request.unit() != null ? request.unit() : "KG";
                    return accountRepository.save(new InventoryAccount(
                            request.ownerType(), request.ownerId(), request.productId(), unit));
                });

        if ("STOCK_ADDITION".equalsIgnoreCase(request.type()) || "INITIAL_STOCK".equalsIgnoreCase(request.type())) {
            account.addStock(request.quantity());
        } else if ("DAMAGED".equalsIgnoreCase(request.type()) || "LOST".equalsIgnoreCase(request.type())) {
            if (account.getAvailableQuantity().compareTo(request.quantity()) < 0) {
                throw new BusinessRuleException("Cannot deduct more than available quantity. Available: "
                        + account.getAvailableQuantity());
            }
            account.deductStock(request.quantity());
        } else {
            // General positive adjustment
            account.addStock(request.quantity());
        }

        InventoryAccount saved = accountRepository.save(account);

        ledgerService.recordTransaction(
                saved.getId(),
                request.type().toUpperCase(),
                request.quantity(),
                request.referenceType(),
                request.referenceId(),
                saved.getAvailableQuantity()
        );

        return InventoryAccountResponse.from(saved);
    }
}
