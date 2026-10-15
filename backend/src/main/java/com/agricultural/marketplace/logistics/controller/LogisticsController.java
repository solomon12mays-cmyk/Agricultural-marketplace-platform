package com.agricultural.marketplace.logistics.controller;

import java.util.List;
import java.util.Map;

import com.agricultural.marketplace.logistics.dto.AssignDriverRequest;
import com.agricultural.marketplace.logistics.dto.CreateLogisticsRequest;
import com.agricultural.marketplace.logistics.dto.LogisticsAssignmentResponse;
import com.agricultural.marketplace.logistics.service.LogisticsAssignmentService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logistics")
public class LogisticsController {

    private final LogisticsAssignmentService logisticsAssignmentService;

    public LogisticsController(LogisticsAssignmentService logisticsAssignmentService) {
        this.logisticsAssignmentService = logisticsAssignmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LogisticsAssignmentResponse>> createAssignment(
            @Valid @RequestBody CreateLogisticsRequest request) {
        LogisticsAssignmentResponse response = logisticsAssignmentService.createAssignment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<ApiResponse<LogisticsAssignmentResponse>> assignDriver(
            @PathVariable Long id,
            @Valid @RequestBody AssignDriverRequest request) {
        LogisticsAssignmentResponse response = logisticsAssignmentService.assignDriver(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.getOrDefault("status", "ASSIGNED");
        logisticsAssignmentService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LogisticsAssignmentResponse>> getAssignment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(logisticsAssignmentService.getAssignmentById(id)));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<LogisticsAssignmentResponse>> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.success(logisticsAssignmentService.getAssignmentByOrderId(orderId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<LogisticsAssignmentResponse>>> listAssignments(
            @RequestParam(required = false) String status,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<LogisticsAssignmentResponse> page = logisticsAssignmentService.listAssignments(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<ApiResponse<List<LogisticsAssignmentResponse>>> listByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(ApiResponse.success(logisticsAssignmentService.listAssignmentsByDriver(driverId)));
    }
}
