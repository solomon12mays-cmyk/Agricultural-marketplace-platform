package com.agricultural.marketplace.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.agricultural.marketplace.inventory.entity.InventoryTransaction;

public record InventoryTransactionResponse(
        Long id,
        Long accountId,
        String type,
        BigDecimal quantity,
        String referenceType,
        String referenceId,
        BigDecimal balanceAfter,
        LocalDateTime createdAt) {

    public static InventoryTransactionResponse from(InventoryTransaction entity) {
        return new InventoryTransactionResponse(
                entity.getId(),
                entity.getAccountId(),
                entity.getType(),
                entity.getQuantity(),
                entity.getReferenceType(),
                entity.getReferenceId(),
                entity.getBalanceAfter(),
                entity.getCreatedAt());
    }
}

