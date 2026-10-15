package com.agricultural.marketplace.shared.exception;

import java.util.Map;

/**
 * Thrown when request data fails validation (HTTP 400).
 */
public class ValidationException extends RuntimeException {

    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        this(message, Map.of());
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors == null ? Map.of() : fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
