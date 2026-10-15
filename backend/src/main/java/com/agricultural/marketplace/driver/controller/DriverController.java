package com.agricultural.marketplace.driver.controller;

import java.util.List;
import java.util.Map;

import com.agricultural.marketplace.auth.entity.User;
import com.agricultural.marketplace.auth.repository.UserRepository;
import com.agricultural.marketplace.driver.dto.CreateDriverRequest;
import com.agricultural.marketplace.driver.dto.CreateVehicleRequest;
import com.agricultural.marketplace.driver.dto.DriverResponse;
import com.agricultural.marketplace.driver.dto.VehicleResponse;
import com.agricultural.marketplace.driver.service.DriverService;
import com.agricultural.marketplace.driver.service.VehicleService;
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
@RequestMapping("/api/v1/drivers")
public class DriverController {

    private final DriverService driverService;
    private final VehicleService vehicleService;
    private final SecurityContextService securityContextService;
    private final UserRepository userRepository;

    public DriverController(DriverService driverService,
                            VehicleService vehicleService,
                            SecurityContextService securityContextService,
                            UserRepository userRepository) {
        this.driverService = driverService;
        this.vehicleService = vehicleService;
        this.securityContextService = securityContextService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DriverResponse>> registerDriver(
            @Valid @RequestBody CreateDriverRequest request) {
        Long userId = request.userId();
        if (userId == null) {
            userId = securityContextService.currentUsername()
                    .flatMap(userRepository::findByUsernameIgnoreCase)
                    .map(User::getId)
                    .orElse(null);
        }
        CreateDriverRequest effectiveRequest = new CreateDriverRequest(
                userId,
                request.fullName(),
                request.licenseNumber(),
                request.licenseExpiresAt(),
                request.phone()
        );
        DriverResponse response = driverService.registerDriver(effectiveRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<DriverResponse>> getCurrentDriver() {
        String username = securityContextService.requireCurrentUsername();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        DriverResponse response = driverService.getDriverByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No driver profile for user: " + username));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverResponse>> getDriver(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(driverService.getDriverById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DriverResponse>>> listDrivers(
            @RequestParam(required = false) String status,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DriverResponse> page = driverService.listDrivers(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<DriverResponse>> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.getOrDefault("status", "ACTIVE");
        return ResponseEntity.ok(ApiResponse.success(driverService.updateDriverStatus(id, status)));
    }

    @PostMapping("/{id}/vehicles")
    public ResponseEntity<ApiResponse<VehicleResponse>> addVehicle(
            @PathVariable Long id,
            @Valid @RequestBody CreateVehicleRequest request) {
        VehicleResponse response = vehicleService.addVehicle(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{id}/vehicles")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> listVehicles(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(vehicleService.getVehiclesByDriver(id)));
    }
}
