package com.agricultural.marketplace.organization.controller;

import java.util.List;

import com.agricultural.marketplace.auth.entity.User;
import com.agricultural.marketplace.auth.repository.UserRepository;
import com.agricultural.marketplace.organization.dto.CreateOrganizationRequest;
import com.agricultural.marketplace.organization.dto.OrganizationDocumentResponse;
import com.agricultural.marketplace.organization.dto.OrganizationResponse;
import com.agricultural.marketplace.organization.dto.OrganizationVerificationRequest;
import com.agricultural.marketplace.organization.dto.UpdateOrganizationRequest;
import com.agricultural.marketplace.organization.dto.UploadDocumentRequest;
import com.agricultural.marketplace.organization.service.OrganizationService;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final SecurityContextService securityContextService;
    private final UserRepository userRepository;

    public OrganizationController(OrganizationService organizationService,
                                  SecurityContextService securityContextService,
                                  UserRepository userRepository) {
        this.organizationService = organizationService;
        this.securityContextService = securityContextService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationResponse>> registerOrganization(
            @Valid @RequestBody CreateOrganizationRequest request) {
        Long userId = request.userId();
        if (userId == null) {
            userId = securityContextService.currentUsername()
                    .flatMap(userRepository::findByUsernameIgnoreCase)
                    .map(User::getId)
                    .orElse(null);
        }
        CreateOrganizationRequest effectiveRequest = new CreateOrganizationRequest(
                userId,
                request.name(),
                request.taxId(),
                request.registrationNumber(),
                request.phone(),
                request.email()
        );
        OrganizationResponse response = organizationService.createOrganization(effectiveRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getCurrentOrganization() {
        String username = securityContextService.requireCurrentUsername();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        OrganizationResponse response = organizationService.getByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No organization profile found for user: " + username));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getOrganization(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(organizationService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OrganizationResponse>>> listOrganizations(
            @RequestParam(required = false) String status,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrganizationResponse> page = organizationService.listOrganizations(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrganizationRequest request) {
        return ResponseEntity.ok(ApiResponse.success(organizationService.updateOrganization(id, request)));
    }

    @PostMapping("/{id}/verification")
    public ResponseEntity<ApiResponse<OrganizationResponse>> updateVerification(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationVerificationRequest request) {
        return ResponseEntity.ok(ApiResponse.success(organizationService.updateVerificationStatus(id, request)));
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<ApiResponse<OrganizationDocumentResponse>> uploadDocument(
            @PathVariable Long id,
            @Valid @RequestBody UploadDocumentRequest request) {
        OrganizationDocumentResponse response = organizationService.uploadDocument(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<ApiResponse<List<OrganizationDocumentResponse>>> listDocuments(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(organizationService.getDocuments(id)));
    }
}
