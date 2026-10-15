package com.agricultural.marketplace.notification.repository;

import java.util.List;

import com.agricultural.marketplace.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    Page<Notification> findByUserId(Long userId, Pageable pageable);

    Page<Notification> findByStatus(String status, Pageable pageable);
}

