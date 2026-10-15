package com.agricultural.marketplace.auth.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * Locks an account temporarily after repeated failed sign-in attempts.
 *
 * <p>TODO: persist attempt counters in Redis so lockouts survive restarts
 * and are shared across instances.</p>
 */
@Service
public class AccountLockService {

    private static final int MAX_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();

    public boolean isLocked(String username) {
        Attempt attempt = attempts.get(username);
        if (attempt == null) {
            return false;
        }
        if (attempt.lockedUntil != null && attempt.lockedUntil.isAfter(LocalDateTime.now())) {
            return true;
        }
        if (attempt.lockedUntil != null && attempt.lockedUntil.isBefore(LocalDateTime.now())) {
            attempts.remove(username);
        }
        return false;
    }

    public void registerFailure(String username) {
        attempts.compute(username, (key, attempt) -> {
            Attempt current = attempt == null ? new Attempt() : attempt;
            current.failedCount++;
            if (current.failedCount >= MAX_ATTEMPTS) {
                current.lockedUntil = LocalDateTime.now().plus(LOCK_DURATION);
            }
            return current;
        });
    }

    public void reset(String username) {
        attempts.remove(username);
    }

    private static final class Attempt {
        private int failedCount;
        private LocalDateTime lockedUntil;
    }
}
