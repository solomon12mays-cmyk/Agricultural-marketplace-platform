package com.agricultural.marketplace.shared.idempotency;

import java.time.LocalDateTime;
import java.util.Optional;

import com.agricultural.marketplace.shared.exception.ConflictException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Guards write endpoints against duplicate submissions.
 *
 * <p>TODO: persist and replay full response payloads, and add a scheduled
 * purge for expired records.</p>
 */
@Service
public class IdempotencyService {

    private static final int DEFAULT_TTL_HOURS = 24;

    private final IdempotencyRepository idempotencyRepository;

    public IdempotencyService(IdempotencyRepository idempotencyRepository) {
        this.idempotencyRepository = idempotencyRepository;
    }

    /**
     * Reserves the idempotency key for a new request.
     *
     * @throws ConflictException when the same key was already used with a
     *         different request payload
     */
    @Transactional
    public IdempotencyRecord begin(String key, String requestHash) {
        Optional<IdempotencyRecord> existing = idempotencyRepository.findByIdempotencyKey(key);
        if (existing.isPresent()) {
            if (!existing.get().getRequestHash().equals(requestHash)) {
                throw new ConflictException("Idempotency key reused with a different request payload");
            }
            return existing.get();
        }
        IdempotencyRecord record = new IdempotencyRecord(key, requestHash,
                LocalDateTime.now().plusHours(DEFAULT_TTL_HOURS));
        return idempotencyRepository.save(record);
    }

    @Transactional
    public void complete(String key, int status, String body) {
        idempotencyRepository.findByIdempotencyKey(key).ifPresent(record -> {
            record.setResponseStatus(status);
            record.setResponseBody(body);
            idempotencyRepository.save(record);
        });
    }

    @Transactional(readOnly = true)
    public Optional<IdempotencyRecord> findCompleted(String key) {
        return idempotencyRepository.findByIdempotencyKey(key)
                .filter(record -> record.getResponseStatus() != null);
    }
}
