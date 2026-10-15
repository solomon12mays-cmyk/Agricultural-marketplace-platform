package com.agricultural.marketplace.notification.provider;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Sends USSD session prompts / push-style USSD messages.
 */
@Service
public class UssdService {

    private static final Logger log = LoggerFactory.getLogger(UssdService.class);

    public String push(String phoneNumber, String message) {
        log.info("Sending USSD push to '{}': {}", phoneNumber, message);
        return "ussd-" + UUID.randomUUID();
    }
}
