package com.agricultural.marketplace.user.controller;

import java.util.List;

import com.agricultural.marketplace.shared.response.ApiResponse;
import com.agricultural.marketplace.shared.response.PageResponse;
import com.agricultural.marketplace.shared.security.SecurityContextService;
import com.agricultural.marketplace.user.dto.AssignRoleRequest;
import com.agricultural.marketplace.user.dto.UpdateUserRequest;
import com.agricultural.marketplace.user.dto.UpdateUserStatusRequest;
import com.agricultural.marketplace.user.dto.UserResponse;
import com.agricultural.marketplace.user.dto.UserSessionResponse;
import com.agricultural.marketplace.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final SecurityContextService securityContextService;

    public UserController(UserService userService, SecurityContextService securityContextService) {
        this.userService = userService;
        this.securityContextService = securityContextService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        String username = securityContextService.requireCurrentUsername();
        UserResponse response = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> listUsers(
            @RequestParam(required = false) String status,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserResponse> page = userService.listUsers(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateUser(id, request)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<UserResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateStatus(id, request.status())));
    }

    @PostMapping("/{id}/unlock")
    public ResponseEntity<ApiResponse<UserResponse>> unlockUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.unlockUser(id)));
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<List<String>>> getUserRoles(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserRoles(id)));
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<List<String>>> assignRole(
            @PathVariable Long id,
            @Valid @RequestBody AssignRoleRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userService.assignRole(id, request.roleCode())));
    }

    @DeleteMapping("/{id}/roles/{roleCode}")
    public ResponseEntity<ApiResponse<List<String>>> revokeRole(
            @PathVariable Long id,
            @PathVariable String roleCode) {
        return ResponseEntity.ok(ApiResponse.success(userService.revokeRole(id, roleCode)));
    }

    @GetMapping("/{id}/sessions")
    public ResponseEntity<ApiResponse<List<UserSessionResponse>>> getUserSessions(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserSessions(id)));
    }

    @DeleteMapping("/{id}/sessions")
    public ResponseEntity<ApiResponse<Void>> revokeAllSessions(@PathVariable Long id) {
        userService.revokeAllSessions(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
