package com.agricultural.marketplace.marketplace.controller;

import java.math.BigDecimal;

import com.agricultural.marketplace.marketplace.dto.MarketplaceItemResponse;
import com.agricultural.marketplace.marketplace.search.MarketplaceSearchService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import com.agricultural.marketplace.shared.response.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public marketplace search and listing details.
 */
@RestController
@RequestMapping("/api/v1/marketplace")
public class MarketplaceController {

    private final MarketplaceSearchService searchService;

    public MarketplaceController(MarketplaceSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<MarketplaceItemResponse>>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<MarketplaceItemResponse> results = searchService.search(
                q, region, category, minPrice, maxPrice, page, size);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @GetMapping("/listings/{id}")
    public ResponseEntity<ApiResponse<MarketplaceItemResponse>> getListingDetails(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(searchService.getListingDetails(id)));
    }
}

