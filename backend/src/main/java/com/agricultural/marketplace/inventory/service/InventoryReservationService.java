package com.agricultural.marketplace.inventory.service;

import java.math.BigDecimal;

import com.agricultural.marketplace.inventory.entity.InventoryAccount;
import com.agricultural.marketplace.inventory.repository.InventoryAccountRepository;
import com.agricultural.marketplace.shared.exception.BusinessRuleException;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryReservationService {

    private final InventoryAccountRepository accountRepository;
    private final InventoryLedgerService ledgerService;

    public InventoryReservationService(InventoryAccountRepository accountRepository,
                                       InventoryLedgerService ledgerService) {
        this.accountRepository = accountRepository;
        this.ledgerService = ledgerService;
    }

    @Transactional
    public Long reserve(Long productId, String ownerType, Long ownerId,
                        BigDecimal quantity, String referenceId) {
        InventoryAccount account = accountRepository.findByOwnerTypeAndOwnerIdAndProductId(ownerType, ownerId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("No inventory account found for owner and product"));

        if (account.getAvailableQuantity().compareTo(quantity) < 0) {
            throw new BusinessRuleException("Insufficient available inventory to reserve. Available: "
                    + account.getAvailableQuantity() + ", requested: " + quantity);
        }

        account.reserveStock(quantity);
        accountRepository.save(account);

        ledgerService.recordTransaction(
                account.getId(),
                "RESERVED",
                quantity,
                "ORDER",
                referenceId,
                account.getAvailableQuantity()
        );

        return account.getId();
    }

    @Transactional
    public void release(Long productId, String ownerType, Long ownerId,
                        BigDecimal quantity, String referenceId) {
        accountRepository.findByOwnerTypeAndOwnerIdAndProductId(ownerType, ownerId, productId)
                .ifPresent(account -> {
                    account.releaseReservation(quantity);
                    accountRepository.save(account);
                    ledgerService.recordTransaction(
                            account.getId(),
                            "RELEASED",
                            quantity,
                            "ORDER",
                            referenceId,
                            account.getAvailableQuantity()
                    );
                });
    }

    @Transactional
    public void consume(Long productId, String ownerType, Long ownerId,
                        BigDecimal quantity, String referenceId) {
        accountRepository.findByOwnerTypeAndOwnerIdAndProductId(ownerType, ownerId, productId)
                .ifPresent(account -> {
                    account.commitReservation(quantity);
                    accountRepository.save(account);
                    ledgerService.recordTransaction(
                            account.getId(),
                            "SOLD",
                            quantity,
                            "ORDER",
                            referenceId,
                            account.getAvailableQuantity()
                    );
                });
    }
}
