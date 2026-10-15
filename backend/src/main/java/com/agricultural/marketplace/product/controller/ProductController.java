package com.agricultural.marketplace.product.controller;

import java.util.List;

import com.agricultural.marketplace.product.dto.CreateGrainVarietyRequest;
import com.agricultural.marketplace.product.dto.CreateProductRequest;
import com.agricultural.marketplace.product.dto.GrainVarietyResponse;
import com.agricultural.marketplace.product.dto.ProductResponse;
import com.agricultural.marketplace.product.service.ProductService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import com.agricultural.marketplace.shared.security.SecurityContextService;
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
 * Product and grain variety endpoints.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final SecurityContextService securityContextService;

    public ProductController(ProductService productService,
                             SecurityContextService securityContextService) {
        this.productService = productService;
        this.securityContextService = securityContextService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> listProducts() {
        return ResponseEntity.ok(ApiResponse.success(productService.getAllProducts()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        String username = securityContextService.currentUsername().orElse("SYSTEM");
        ProductResponse response = productService.createProduct(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/varieties")
    public ResponseEntity<ApiResponse<List<GrainVarietyResponse>>> listVarieties(
            @RequestParam(required = false) String cropType) {
        List<GrainVarietyResponse> varieties = cropType != null && !cropType.isBlank()
                ? productService.getVarietiesByCropType(cropType)
                : productService.getAllVarieties();
        return ResponseEntity.ok(ApiResponse.success(varieties));
    }

    @PostMapping("/varieties")
    public ResponseEntity<ApiResponse<GrainVarietyResponse>> createVariety(
            @Valid @RequestBody CreateGrainVarietyRequest request) {
        GrainVarietyResponse response = productService.createVariety(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }
}
