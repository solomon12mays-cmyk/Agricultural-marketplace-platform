package com.agricultural.marketplace.auth.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agricultural.marketplace.auth.entity.UserSession;

/**
 * Persistence for issued sessions/refresh tokens.
 */
@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findByRefreshTokenHash(String refreshTokenHash);

    java.util.List<UserSession> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    void deleteByUserIdAndRevokedAtIsNull(Long userId);

    void deleteByExpiresAtBefore(LocalDateTime cutoff);
}
