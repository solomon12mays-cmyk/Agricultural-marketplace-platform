package com.agricultural.marketplace.notification.provider;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Sends SMS messages through an aggregator.
 */
@Service
public class SmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);

    public String send(String phoneNumber, String message) {
        log.info("Sending SMS to '{}': {}", phoneNumber, message);
        return "sms-" + UUID.randomUUID();
    }
}
