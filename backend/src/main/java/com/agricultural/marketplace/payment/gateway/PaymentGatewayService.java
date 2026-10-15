package com.agricultural.marketplace.payment.gateway;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

/**
 * Adapter over the external payment provider.
 *
 * <p>TODO: select a provider (telebirr, CBE Birr, card processor), implement
 * authorize/capture/refund/verify calls and normalize gateway errors into
 * {@link PaymentGatewayResult}.</p>
 */
@Service
public class PaymentGatewayService {

    public PaymentGatewayResult authorize(String paymentReference, BigDecimal amount, String currency) {
        // TODO: call the gateway authorize endpoint.
        throw new UnsupportedOperationException("Payment gateway not implemented yet");
    }

    public PaymentGatewayResult capture(String paymentReference, BigDecimal amount, String currency) {
        // TODO: call the gateway capture endpoint.
        throw new UnsupportedOperationException("Payment gateway not implemented yet");
    }

    public PaymentGatewayResult refund(String paymentReference, BigDecimal amount, String currency) {
        // TODO: call the gateway refund endpoint.
        throw new UnsupportedOperationException("Payment gateway not implemented yet");
    }

    public PaymentGatewayResult verify(String paymentReference) {
        // TODO: query the gateway for the authoritative transaction status.
        throw new UnsupportedOperationException("Payment gateway not implemented yet");
    }
}
