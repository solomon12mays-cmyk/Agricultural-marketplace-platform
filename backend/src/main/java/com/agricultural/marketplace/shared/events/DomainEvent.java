package com.agricultural.marketplace.shared.events;

import java.time.LocalDateTime;

/**
 * Marker contract for events raised by the domain.
 */
public interface DomainEvent {

    String getEventType();

    LocalDateTime getOccurredAt();
}
