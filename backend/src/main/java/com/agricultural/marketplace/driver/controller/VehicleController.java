package com.agricultural.marketplace.driver.controller;

import java.util.Map;

import com.agricultural.marketplace.driver.dto.VehicleResponse;
import com.agricultural.marketplace.driver.service.VehicleService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(vehicleService.getVehicleById(id)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.getOrDefault("status", "ACTIVE");
        return ResponseEntity.ok(ApiResponse.success(vehicleService.updateVehicleStatus(id, status)));
    }
}

