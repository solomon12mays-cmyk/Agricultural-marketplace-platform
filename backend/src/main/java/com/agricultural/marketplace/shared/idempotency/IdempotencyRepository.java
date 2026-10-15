package com.agricultural.marketplace.shared.idempotency;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persistence for {@link IdempotencyRecord}.
 */
public interface IdempotencyRepository extends JpaRepository<IdempotencyRecord, Long> {

    Optional<IdempotencyRecord> findByIdempotencyKey(String idempotencyKey);

    void deleteByExpiresAtBefore(java.time.LocalDateTime cutoff);
}
