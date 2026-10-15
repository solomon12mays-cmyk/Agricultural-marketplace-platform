package com.agricultural.marketplace.compliance.controller;

import java.util.List;

import com.agricultural.marketplace.compliance.dto.ComplianceCaseResponse;
import com.agricultural.marketplace.compliance.dto.OpenComplianceCaseRequest;
import com.agricultural.marketplace.compliance.dto.ResolveComplianceCaseRequest;
import com.agricultural.marketplace.compliance.service.ComplianceService;
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
@RequestMapping("/api/v1/compliance/cases")
public class ComplianceController {

    private final ComplianceService complianceService;
    private final SecurityContextService securityContextService;

    public ComplianceController(ComplianceService complianceService,
                                SecurityContextService securityContextService) {
        this.complianceService = complianceService;
        this.securityContextService = securityContextService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ComplianceCaseResponse>> openCase(
            @Valid @RequestBody OpenComplianceCaseRequest request) {
        String openedBy = securityContextService.currentUsername().orElse("SYSTEM");
        ComplianceCaseResponse response = complianceService.openCase(request, openedBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<ComplianceCaseResponse>> resolveCase(
            @PathVariable Long id,
            @Valid @RequestBody ResolveComplianceCaseRequest request) {
        ComplianceCaseResponse response = complianceService.resolveCase(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ComplianceCaseResponse>> getCase(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(complianceService.getCaseById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ComplianceCaseResponse>>> listCases(
            @RequestParam(required = false) String status,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ComplianceCaseResponse> page = complianceService.listCases(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }

    @GetMapping("/subject")
    public ResponseEntity<ApiResponse<List<ComplianceCaseResponse>>> listBySubject(
            @RequestParam String subjectType,
            @RequestParam String subjectId) {
        return ResponseEntity.ok(ApiResponse.success(complianceService.listBySubject(subjectType, subjectId)));
    }
}
