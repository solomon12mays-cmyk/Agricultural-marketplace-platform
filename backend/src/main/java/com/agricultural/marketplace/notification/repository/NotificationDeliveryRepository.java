package com.agricultural.marketplace.notification.repository;

import java.util.List;

import com.agricultural.marketplace.notification.entity.NotificationDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationDeliveryRepository extends JpaRepository<NotificationDelivery, Long> {

    List<NotificationDelivery> findByNotificationId(Long notificationId);
}

