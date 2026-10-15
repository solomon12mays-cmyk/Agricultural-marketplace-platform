package com.agricultural.marketplace.compliance.controller;

import java.util.List;

import com.agricultural.marketplace.compliance.dto.DisputeResponse;
import com.agricultural.marketplace.compliance.dto.OpenDisputeRequest;
import com.agricultural.marketplace.compliance.dto.ResolveDisputeRequest;
import com.agricultural.marketplace.compliance.service.DisputeService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import com.agricultural.marketplace.shared.response.PageResponse;
import com.agricultural.marketplace.shared.security.SecurityContextService;
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
@RequestMapping("/api/v1/disputes")
public class DisputeController {

    private final DisputeService disputeService;
    private final SecurityContextService securityContextService;

    public DisputeController(DisputeService disputeService,
                             SecurityContextService securityContextService) {
        this.disputeService = disputeService;
        this.securityContextService = securityContextService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DisputeResponse>> openDispute(
            @Valid @RequestBody OpenDisputeRequest request) {
        String openedBy = securityContextService.currentUsername().orElse("SYSTEM");
        DisputeResponse response = disputeService.openDispute(request, openedBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<DisputeResponse>> resolveDispute(
            @PathVariable Long id,
            @Valid @RequestBody ResolveDisputeRequest request) {
        DisputeResponse response = disputeService.resolveDispute(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DisputeResponse>> getDispute(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(disputeService.getDisputeById(id)));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<DisputeResponse>>> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.success(disputeService.getDisputesByOrderId(orderId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DisputeResponse>>> listDisputes(
            @RequestParam(required = false) String status,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DisputeResponse> page = disputeService.listDisputes(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }
}
