package com.agricultural.marketplace.delivery.controller;

import com.agricultural.marketplace.delivery.dto.ConfirmDeliveryRequest;
import com.agricultural.marketplace.delivery.dto.DeliveryResponse;
import com.agricultural.marketplace.delivery.dto.FailDeliveryRequest;
import com.agricultural.marketplace.delivery.service.DeliveryService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import com.agricultural.marketplace.shared.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/orders/{orderId}/confirm")
    public ResponseEntity<ApiResponse<DeliveryResponse>> confirmDelivery(
            @PathVariable Long orderId,
            @Valid @RequestBody ConfirmDeliveryRequest request) {
        DeliveryResponse response = deliveryService.recordDelivery(orderId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/orders/{orderId}/fail")
    public ResponseEntity<ApiResponse<Void>> failDelivery(
            @PathVariable Long orderId,
            @Valid @RequestBody FailDeliveryRequest request) {
        deliveryService.reportFailure(orderId, request.reason());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeliveryResponse>> getDelivery(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(deliveryService.getDeliveryById(id)));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<DeliveryResponse>> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.success(deliveryService.getDeliveryByOrderId(orderId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DeliveryResponse>>> listDeliveries(
            @RequestParam(required = false) String status,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DeliveryResponse> page = deliveryService.listDeliveries(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }
}
