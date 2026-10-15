package com.agricultural.marketplace.auth.controller;

import java.util.Map;

import com.agricultural.marketplace.auth.dto.ChangePasswordRequest;
import com.agricultural.marketplace.auth.dto.LoginRequest;
import com.agricultural.marketplace.auth.dto.LoginResponse;
import com.agricultural.marketplace.auth.dto.PasswordRecoveryRequest;
import com.agricultural.marketplace.auth.dto.RefreshTokenRequest;
import com.agricultural.marketplace.auth.dto.RegisterRequest;
import com.agricultural.marketplace.auth.service.AuthService;
import com.agricultural.marketplace.auth.service.RecoveryService;
import com.agricultural.marketplace.shared.security.SecurityContextService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication endpoints: login, registration, token refresh and
 * password management.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RecoveryService recoveryService;
    private final SecurityContextService securityContextService;

    public AuthController(AuthService authService,
                          RecoveryService recoveryService,
                          SecurityContextService securityContextService) {
        this.authService = authService;
        this.recoveryService = recoveryService;
        this.securityContextService = securityContextService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Long>> register(@Valid @RequestBody RegisterRequest request) {
        Long userId = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("userId", userId));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(securityContextService.requireCurrentUsername(), request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password-recovery")
    public ResponseEntity<Void> requestRecovery(@Valid @RequestBody PasswordRecoveryRequest request) {
        recoveryService.requestRecovery(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/password-recovery/reset")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody Map<String, String> request) {
        recoveryService.resetPassword(request.get("identifier"), request.get("code"), request.get("newPassword"));
        return ResponseEntity.noContent().build();
    }
}
