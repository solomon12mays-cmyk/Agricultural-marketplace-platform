package com.agricultural.marketplace.ledger.repository;

import java.util.List;

import com.agricultural.marketplace.ledger.entity.JournalLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalLineRepository extends JpaRepository<JournalLine, Long> {

    List<JournalLine> findByEntryId(Long entryId);
}

