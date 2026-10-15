package com.agricultural.marketplace.shared.exception;

/**
 * Thrown when authentication is missing or invalid (HTTP 401).
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
