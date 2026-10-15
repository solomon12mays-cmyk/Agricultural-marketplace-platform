package com.agricultural.marketplace.inventory.service;

import java.math.BigDecimal;
import java.util.List;

import com.agricultural.marketplace.inventory.dto.InventoryTransactionResponse;
import com.agricultural.marketplace.inventory.entity.InventoryTransaction;
import com.agricultural.marketplace.inventory.repository.InventoryTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InventoryLedgerService {

    private final InventoryTransactionRepository transactionRepository;

    public InventoryLedgerService(InventoryTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public InventoryTransaction recordTransaction(Long accountId, String type, BigDecimal quantity,
                                                 String referenceType, String referenceId, BigDecimal balanceAfter) {
        InventoryTransaction tx = new InventoryTransaction(
                accountId, type, quantity, referenceType, referenceId, balanceAfter);
        return transactionRepository.save(tx);
    }

    public List<InventoryTransactionResponse> statement(Long accountId) {
        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId).stream()
                .map(InventoryTransactionResponse::from)
                .toList();
    }
}
