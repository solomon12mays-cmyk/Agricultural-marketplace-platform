package com.agricultural.marketplace.payment.gateway;

/**
 * Outcome of a payment gateway call.
 */
public record PaymentGatewayResult(
        boolean successful,
        String transactionReference,
        String message,
        String rawResponse) {

    public static PaymentGatewayResult success(String transactionReference, String rawResponse) {
        return new PaymentGatewayResult(true, transactionReference, "OK", rawResponse);
    }

    public static PaymentGatewayResult failure(String message, String rawResponse) {
        return new PaymentGatewayResult(false, null, message, rawResponse);
    }
}
