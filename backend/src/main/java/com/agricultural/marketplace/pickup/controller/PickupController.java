package com.agricultural.marketplace.pickup.controller;

import com.agricultural.marketplace.pickup.dto.ConfirmPickupRequest;
import com.agricultural.marketplace.pickup.dto.PickupResponse;
import com.agricultural.marketplace.pickup.dto.SchedulePickupRequest;
import com.agricultural.marketplace.pickup.service.PickupService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import com.agricultural.marketplace.shared.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pickups")
public class PickupController {

    private final PickupService pickupService;

    public PickupController(PickupService pickupService) {
        this.pickupService = pickupService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PickupResponse>> schedulePickup(
            @Valid @RequestBody SchedulePickupRequest request) {
        PickupResponse response = pickupService.schedulePickup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<PickupResponse>> confirmPickup(
            @PathVariable Long id,
            @Valid @RequestBody ConfirmPickupRequest request) {
        PickupResponse response = pickupService.confirmPickup(id, request.receivedBy());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PickupResponse>> getPickup(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(pickupService.getPickupById(id)));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<PickupResponse>> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.success(pickupService.getPickupByOrderId(orderId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PickupResponse>>> listPickups(
            @RequestParam(required = false) String status,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PickupResponse> page = pickupService.listPickups(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }
}
