package com.agricultural.marketplace.inventory.repository;

import java.util.List;
import java.util.Optional;

import com.agricultural.marketplace.inventory.entity.InventoryAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryAccountRepository extends JpaRepository<InventoryAccount, Long> {

    Optional<InventoryAccount> findByOwnerTypeAndOwnerIdAndProductId(
            String ownerType, Long ownerId, Long productId);

    List<InventoryAccount> findByOwnerTypeAndOwnerId(String ownerType, Long ownerId);
}

