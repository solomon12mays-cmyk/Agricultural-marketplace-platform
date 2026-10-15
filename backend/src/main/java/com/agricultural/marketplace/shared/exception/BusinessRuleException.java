package com.agricultural.marketplace.shared.exception;

/**
 * Thrown when an operation violates a business rule, such as an illegal
 * state transition (HTTP 422).
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
