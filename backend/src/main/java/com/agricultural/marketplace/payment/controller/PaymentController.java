package com.agricultural.marketplace.payment.controller;

import java.math.BigDecimal;
import java.util.List;

import com.agricultural.marketplace.payment.dto.CheckoutPaymentRequest;
import com.agricultural.marketplace.payment.dto.ConfirmPaymentRequest;
import com.agricultural.marketplace.payment.dto.PaymentResponse;
import com.agricultural.marketplace.payment.service.PaymentService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Payment endpoints: checkout, confirmation, lookup and refund.
 */
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<PaymentResponse>> checkout(
            @Valid @RequestBody CheckoutPaymentRequest request) {
        PaymentResponse response = paymentService.initiatePayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<PaymentResponse>> confirm(
            @PathVariable Long id,
            @Valid @RequestBody ConfirmPaymentRequest request) {
        PaymentResponse response = paymentService.confirmPayment(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getPaymentById(id)));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getPaymentByOrderId(orderId)));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<ApiResponse<PaymentResponse>> refund(
            @PathVariable Long id,
            @RequestParam(required = false) BigDecimal amount,
            @RequestParam(required = false) String reason) {
        PaymentResponse response = paymentService.refund(id, amount, reason);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> listPayments(
            @RequestParam String payerType,
            @RequestParam Long payerId) {
        return ResponseEntity.ok(ApiResponse.success(
                paymentService.getPaymentsByPayer(payerType, payerId)));
    }
}

