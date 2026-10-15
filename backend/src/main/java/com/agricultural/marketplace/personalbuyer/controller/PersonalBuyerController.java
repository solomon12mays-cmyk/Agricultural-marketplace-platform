package com.agricultural.marketplace.personalbuyer.controller;

import com.agricultural.marketplace.auth.entity.User;
import com.agricultural.marketplace.auth.repository.UserRepository;
import com.agricultural.marketplace.personalbuyer.dto.CreatePersonalBuyerRequest;
import com.agricultural.marketplace.personalbuyer.dto.PersonalBuyerResponse;
import com.agricultural.marketplace.personalbuyer.dto.UpdateKycStatusRequest;
import com.agricultural.marketplace.personalbuyer.dto.UpdatePersonalBuyerRequest;
import com.agricultural.marketplace.personalbuyer.service.PersonalBuyerService;
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
@RequestMapping("/api/v1/personal-buyers")
public class PersonalBuyerController {

    private final PersonalBuyerService personalBuyerService;
    private final SecurityContextService securityContextService;
    private final UserRepository userRepository;

    public PersonalBuyerController(PersonalBuyerService personalBuyerService,
                                   SecurityContextService securityContextService,
                                   UserRepository userRepository) {
        this.personalBuyerService = personalBuyerService;
        this.securityContextService = securityContextService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PersonalBuyerResponse>> register(
            @Valid @RequestBody CreatePersonalBuyerRequest request) {
        Long userId = request.userId();
        if (userId == null) {
            userId = securityContextService.currentUsername()
                    .flatMap(userRepository::findByUsernameIgnoreCase)
                    .map(User::getId)
                    .orElse(null);
        }
        CreatePersonalBuyerRequest effectiveRequest = new CreatePersonalBuyerRequest(
                userId,
                request.fullName(),
                request.phone(),
                request.email(),
                request.region()
        );
        PersonalBuyerResponse response = personalBuyerService.createPersonalBuyer(effectiveRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PersonalBuyerResponse>> getCurrent() {
        String username = securityContextService.requireCurrentUsername();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        PersonalBuyerResponse response = personalBuyerService.getByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No personal buyer profile for user: " + username));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonalBuyerResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(personalBuyerService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PersonalBuyerResponse>>> list(
            @RequestParam(required = false) String kycStatus,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PersonalBuyerResponse> page = personalBuyerService.listPersonalBuyers(kycStatus, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonalBuyerResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePersonalBuyerRequest request) {
        return ResponseEntity.ok(ApiResponse.success(personalBuyerService.updatePersonalBuyer(id, request)));
    }

    @PostMapping("/{id}/kyc")
    public ResponseEntity<ApiResponse<PersonalBuyerResponse>> updateKyc(
            @PathVariable Long id,
            @Valid @RequestBody UpdateKycStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success(personalBuyerService.updateKycStatus(id, request)));
    }
}
