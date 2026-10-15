package com.agricultural.marketplace.shared.response;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Machine-readable error payload returned with every failed request.
 */
public record ApiError(
        String code,
        String message,
        Map<String, String> fieldErrors,
        LocalDateTime timestamp) {

    public static ApiError of(String code, String message) {
        return new ApiError(code, message, Map.of(), LocalDateTime.now());
    }

    public static ApiError of(String code, String message, Map<String, String> fieldErrors) {
        return new ApiError(code, message, fieldErrors == null ? Map.of() : fieldErrors, LocalDateTime.now());
    }
}
