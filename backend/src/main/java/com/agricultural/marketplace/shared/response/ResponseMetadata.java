package com.agricultural.marketplace.shared.response;

import java.time.LocalDateTime;

/**
 * Metadata attached to every API response.
 */
public record ResponseMetadata(
        LocalDateTime timestamp,
        String correlationId) {

    public static ResponseMetadata empty() {
        return new ResponseMetadata(LocalDateTime.now(), null);
    }

    public static ResponseMetadata withCorrelationId(String correlationId) {
        return new ResponseMetadata(LocalDateTime.now(), correlationId);
    }
}
