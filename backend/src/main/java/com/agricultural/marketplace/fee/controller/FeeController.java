package com.agricultural.marketplace.fee.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.agricultural.marketplace.fee.entity.FeeRule;
import com.agricultural.marketplace.fee.service.FeeCalculationService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fee rule and calculation endpoints.
 */
@RestController
@RequestMapping("/api/v1/fees")
public class FeeController {

    private final FeeCalculationService feeCalculationService;

    public FeeController(FeeCalculationService feeCalculationService) {
        this.feeCalculationService = feeCalculationService;
    }

    @GetMapping("/rules")
    public ResponseEntity<ApiResponse<List<FeeRule>>> listFeeRules() {
        return ResponseEntity.ok(ApiResponse.success(feeCalculationService.getActiveFeeRules()));
    }

    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> calculateFee(
            @RequestParam String feeType,
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "ETB") String currency) {
        BigDecimal fee = feeCalculationService.calculate(feeType, "PREVIEW", "0", amount, currency);
        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "baseAmount", amount,
                "feeAmount", fee,
                "currency", currency,
                "netAmount", amount.subtract(fee))));
    }
}

