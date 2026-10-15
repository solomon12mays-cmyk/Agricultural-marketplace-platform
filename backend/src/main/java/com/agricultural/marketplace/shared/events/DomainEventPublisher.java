package com.agricultural.marketplace.shared.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publishes domain events to in-process listeners; async delivery to
 * external systems will build on top of this.
 */
@Component
public class DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DomainEventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public DomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(DomainEvent event) {
        log.debug("Publishing domain event {}", event.getEventType());
        applicationEventPublisher.publishEvent(event);
    }
}
