package com.agricultural.marketplace.ledger.repository;

import java.util.List;
import java.util.Optional;

import com.agricultural.marketplace.ledger.entity.LedgerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerAccountRepository extends JpaRepository<LedgerAccount, Long> {

    Optional<LedgerAccount> findByCode(String code);

    List<LedgerAccount> findByAccountType(String accountType);
}

