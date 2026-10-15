package com.agricultural.marketplace.inventory.repository;

import java.util.List;

import com.agricultural.marketplace.inventory.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {

    List<InventoryTransaction> findByAccountIdOrderByCreatedAtDesc(Long accountId);
}

