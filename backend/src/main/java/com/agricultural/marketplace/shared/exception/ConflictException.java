package com.agricultural.marketplace.shared.exception;

/**
 * Thrown when the request conflicts with current state, such as a duplicate
 * unique value (HTTP 409).
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
