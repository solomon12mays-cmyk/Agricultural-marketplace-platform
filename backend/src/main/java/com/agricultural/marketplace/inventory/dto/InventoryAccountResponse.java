package com.agricultural.marketplace.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.agricultural.marketplace.inventory.entity.InventoryAccount;

public record InventoryAccountResponse(
        Long id,
        String ownerType,
        Long ownerId,
        Long productId,
        BigDecimal quantity,
        BigDecimal reservedQuantity,
        BigDecimal availableQuantity,
        String unit,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static InventoryAccountResponse from(InventoryAccount entity) {
        return new InventoryAccountResponse(
                entity.getId(),
                entity.getOwnerType(),
                entity.getOwnerId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getReservedQuantity(),
                entity.getAvailableQuantity(),
                entity.getUnit(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}

