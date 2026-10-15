package com.agricultural.marketplace.listing.controller;

import java.util.List;

import com.agricultural.marketplace.listing.dto.CreateListingRequest;
import com.agricultural.marketplace.listing.dto.ListingResponse;
import com.agricultural.marketplace.listing.dto.UpdateListingRequest;
import com.agricultural.marketplace.listing.service.ListingService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import jakarta.validation.Valid;
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

/**
 * Listing endpoints.
 */
@RestController
@RequestMapping("/api/v1/listings")
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ListingResponse>> createListing(
            @Valid @RequestBody CreateListingRequest request) {
        ListingResponse response = listingService.createListing(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ListingResponse>>> listListings(
            @RequestParam(required = false) String sellerType,
            @RequestParam(required = false) Long sellerId) {
        if (sellerType != null && sellerId != null) {
            return ResponseEntity.ok(ApiResponse.success(
                    listingService.getListingsBySeller(sellerType, sellerId)));
        }
        return ResponseEntity.ok(ApiResponse.success(listingService.getActiveListings()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ListingResponse>> getListing(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(listingService.getListingById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ListingResponse>> updateListing(
            @PathVariable Long id,
            @Valid @RequestBody UpdateListingRequest request) {
        return ResponseEntity.ok(ApiResponse.success(listingService.updateListing(id, request)));
    }

    @PostMapping("/{id}/pause")
    public ResponseEntity<ApiResponse<ListingResponse>> pauseListing(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(listingService.pauseListing(id)));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<ListingResponse>> activateListing(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(listingService.activateListing(id)));
    }
}

