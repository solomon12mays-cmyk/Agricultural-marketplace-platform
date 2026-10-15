package com.agricultural.marketplace.shared.exception;

/**
 * Thrown when the caller is authenticated but not allowed to perform the
 * operation (HTTP 403).
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
