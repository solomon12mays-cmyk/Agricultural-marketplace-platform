package com.agricultural.marketplace.shared.locking;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Mutual exclusion for distributed operations such as stock reservation,
 * payout generation and reconciliation runs.
 */
@Service
public class DistributedLockService {

    private final StringRedisTemplate redisTemplate;

    public DistributedLockService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Attempts to acquire the lock; returns {@code false} when it is held
     * elsewhere.
     */
    public boolean tryLock(String key, String token, Duration ttl) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, token, ttl));
    }

    /**
     * Releases the lock only when it is still owned by {@code token}.
     */
    public void unlock(String key, String token) {
        String current = redisTemplate.opsForValue().get(key);
        if (token != null && token.equals(current)) {
            redisTemplate.delete(key);
        }
    }
}
