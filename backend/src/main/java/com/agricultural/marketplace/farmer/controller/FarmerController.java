package com.agricultural.marketplace.farmer.controller;

import java.util.List;

import com.agricultural.marketplace.auth.entity.User;
import com.agricultural.marketplace.auth.repository.UserRepository;
import com.agricultural.marketplace.farmer.dto.CreateFarmerProfileRequest;
import com.agricultural.marketplace.farmer.dto.FarmerResponse;
import com.agricultural.marketplace.farmer.dto.UpdateFarmerProfileRequest;
import com.agricultural.marketplace.farmer.service.FarmerService;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import com.agricultural.marketplace.shared.response.ApiResponse;
import com.agricultural.marketplace.shared.security.SecurityContextService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Farmer profile endpoints.
 */
@RestController
@RequestMapping("/api/v1/farmers")
public class FarmerController {

    private final FarmerService farmerService;
    private final SecurityContextService securityContextService;
    private final UserRepository userRepository;

    public FarmerController(FarmerService farmerService,
                            SecurityContextService securityContextService,
                            UserRepository userRepository) {
        this.farmerService = farmerService;
        this.securityContextService = securityContextService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FarmerResponse>> registerProfile(
            @Valid @RequestBody CreateFarmerProfileRequest request) {
        Long userId = securityContextService.currentUsername()
                .flatMap(userRepository::findByUsernameIgnoreCase)
                .map(User::getId)
                .orElse(null);

        FarmerResponse response = farmerService.registerFarmer(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<FarmerResponse>> getCurrentProfile() {
        String username = securityContextService.requireCurrentUsername();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        FarmerResponse response = farmerService.getProfileByUserId(user.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FarmerResponse>> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(farmerService.getProfileById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FarmerResponse>>> listFarmers() {
        return ResponseEntity.ok(ApiResponse.success(farmerService.getAllFarmers()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FarmerResponse>> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFarmerProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.success(farmerService.updateProfile(id, request)));
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<ApiResponse<FarmerResponse>> verifyFarmer(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(farmerService.verifyFarmer(id)));
    }
}
