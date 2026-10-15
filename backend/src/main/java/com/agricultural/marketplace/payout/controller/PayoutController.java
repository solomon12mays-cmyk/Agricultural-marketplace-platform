package com.agricultural.marketplace.payout.controller;

import java.util.List;

import com.agricultural.marketplace.payout.entity.Payout;
import com.agricultural.marketplace.payout.service.PayoutService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Payout disbursement endpoints.
 */
@RestController
@RequestMapping("/api/v1/payouts")
public class PayoutController {

    private final PayoutService payoutService;

    public PayoutController(PayoutService payoutService) {
        this.payoutService = payoutService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Payout>>> listPayouts(
            @RequestParam(required = false) String payeeType,
            @RequestParam(required = false) Long payeeId) {
        if (payeeType != null && payeeId != null) {
            return ResponseEntity.ok(ApiResponse.success(
                    payoutService.getPayoutsByPayee(payeeType, payeeId)));
        }
        return ResponseEntity.ok(ApiResponse.success(payoutService.getAllPayouts()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Payout>> getPayout(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(payoutService.getPayoutById(id)));
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<ApiResponse<Payout>> processPayout(
            @PathVariable Long id,
            @RequestParam(required = false) String reference) {
        return ResponseEntity.ok(ApiResponse.success(payoutService.processPayout(id, reference)));
    }
}

