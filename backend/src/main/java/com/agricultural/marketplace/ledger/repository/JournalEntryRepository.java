package com.agricultural.marketplace.ledger.repository;

import java.util.List;

import com.agricultural.marketplace.ledger.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    List<JournalEntry> findByReferenceTypeAndReferenceId(String referenceType, String referenceId);
}

