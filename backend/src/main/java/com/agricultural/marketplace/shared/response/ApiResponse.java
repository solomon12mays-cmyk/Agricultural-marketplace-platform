package com.agricultural.marketplace.shared.response;

/**
 * Standard envelope for successful API responses.
 */
public record ApiResponse<T>(T data, ResponseMetadata metadata) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, ResponseMetadata.empty());
    }

    public static <T> ApiResponse<T> success(T data, String correlationId) {
        return new ApiResponse<>(data, ResponseMetadata.withCorrelationId(correlationId));
    }
}
