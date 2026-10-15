package com.agricultural.marketplace.notification.provider;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Sends mobile/web push notifications.
 */
@Service
public class PushNotificationService {

    private static final Logger log = LoggerFactory.getLogger(PushNotificationService.class);

    public String send(String deviceToken, String title, String body) {
        log.info("Sending push notification to token '{}': [{}] {}", deviceToken, title, body);
        return "push-" + UUID.randomUUID();
    }
}
