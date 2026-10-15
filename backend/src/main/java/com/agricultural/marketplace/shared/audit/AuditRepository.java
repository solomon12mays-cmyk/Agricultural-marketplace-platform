package com.agricultural.marketplace.shared.audit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persistence for {@link AuditEvent}.
 */
public interface AuditRepository extends JpaRepository<AuditEvent, Long> {

    List<AuditEvent> findByAggregateTypeAndAggregateIdOrderByCreatedAtDesc(String aggregateType, String aggregateId);
}
