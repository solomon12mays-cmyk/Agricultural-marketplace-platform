package com.agricultural.marketplace.shared.audit;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Records audit trail entries.
 *
 * <p>TODO: enrich entries with request metadata (ip, user agent) and
 * publish audit events downstream.</p>
 */
@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    private final AuditRepository auditRepository;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AuditEvent record(String aggregateType, String aggregateId, AuditAction action,
                             String actorId, String details) {
        AuditEvent event = new AuditEvent(aggregateType, aggregateId, action, actorId, details, LocalDateTime.now());
        return auditRepository.save(event);
    }

    @Transactional(readOnly = true)
    public java.util.List<AuditEvent> historyFor(String aggregateType, String aggregateId) {
        return auditRepository.findByAggregateTypeAndAggregateIdOrderByCreatedAtDesc(aggregateType, aggregateId);
    }
}
