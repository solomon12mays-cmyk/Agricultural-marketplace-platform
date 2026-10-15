package com.agricultural.marketplace.farm.controller;

import java.util.List;

import com.agricultural.marketplace.farm.dto.CreateFarmLocationRequest;
import com.agricultural.marketplace.farm.dto.CreateFarmRequest;
import com.agricultural.marketplace.farm.dto.FarmLocationResponse;
import com.agricultural.marketplace.farm.dto.FarmResponse;
import com.agricultural.marketplace.farm.service.FarmService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Farm and farm location endpoints.
 */
@RestController
@RequestMapping("/api/v1/farms")
public class FarmController {

    private final FarmService farmService;

    public FarmController(FarmService farmService) {
        this.farmService = farmService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FarmResponse>> createFarm(
            @Valid @RequestBody CreateFarmRequest request) {
        FarmResponse response = farmService.createFarm(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FarmResponse>>> listFarms(
            @RequestParam(required = false) Long farmerId) {
        if (farmerId != null) {
            return ResponseEntity.ok(ApiResponse.success(farmService.getFarmsByFarmerId(farmerId)));
        }
        return ResponseEntity.ok(ApiResponse.success(List.of()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FarmResponse>> getFarm(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(farmService.getFarmById(id)));
    }

    @PostMapping("/{id}/location")
    public ResponseEntity<ApiResponse<FarmLocationResponse>> setFarmLocation(
            @PathVariable Long id,
            @Valid @RequestBody CreateFarmLocationRequest request) {
        FarmLocationResponse response = farmService.setFarmLocation(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
