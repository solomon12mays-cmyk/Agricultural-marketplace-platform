package com.agricultural.marketplace.shared.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Baseline listener for domain events.
 *
 * <p>TODO: replace the catch-all logging with concrete per-event handlers
 * (notifications, search indexing, outbox dispatch).</p>
 */
@Component
public class DomainEventListener {

    private static final Logger log = LoggerFactory.getLogger(DomainEventListener.class);

    @Async
    @EventListener
    public void onDomainEvent(DomainEvent event) {
        log.info("Domain event received: {} at {}", event.getEventType(), event.getOccurredAt());
    }
}
