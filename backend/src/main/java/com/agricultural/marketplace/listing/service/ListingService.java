package com.agricultural.marketplace.listing.service;

import java.math.BigDecimal;
import java.util.List;

import com.agricultural.marketplace.inventory.service.InventoryService;
import com.agricultural.marketplace.listing.dto.CreateListingRequest;
import com.agricultural.marketplace.listing.dto.ListingResponse;
import com.agricultural.marketplace.listing.dto.UpdateListingRequest;
import com.agricultural.marketplace.listing.entity.Listing;
import com.agricultural.marketplace.listing.repository.ListingRepository;
import com.agricultural.marketplace.listing.state.ListingStateMachine;
import com.agricultural.marketplace.product.entity.Product;
import com.agricultural.marketplace.product.repository.ProductRepository;
import com.agricultural.marketplace.shared.exception.BusinessRuleException;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListingService {

    private final ListingRepository listingRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    public ListingService(ListingRepository listingRepository,
                          ProductRepository productRepository,
                          InventoryService inventoryService) {
        this.listingRepository = listingRepository;
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
    }

    public List<ListingResponse> getListingsBySeller(String sellerType, Long sellerId) {
        return listingRepository.findBySellerTypeAndSellerId(sellerType, sellerId).stream()
                .map(this::toResponse)
                .toList();
    }

    public ListingResponse getListingById(Long id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + id));
        return toResponse(listing);
    }

    public List<ListingResponse> getActiveListings() {
        return listingRepository.findByStatus("ACTIVE").stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ListingResponse createListing(CreateListingRequest request) {
        // Enforce Rule 16: Listing quantity must never exceed available inventory
        BigDecimal available = inventoryService.availableQuantity(
                request.sellerType(), request.sellerId(), request.productId());
        if (available.compareTo(request.quantityAvailable()) < 0) {
            throw new BusinessRuleException(
                    "Listing quantity (" + request.quantityAvailable()
                            + ") exceeds available inventory (" + available + ")");
        }

        String currency = request.currency() != null ? request.currency() : "ETB";
        Listing listing = new Listing(
                request.sellerType(),
                request.sellerId(),
                request.productId(),
                request.quantityAvailable(),
                request.unit(),
                request.pricePerUnit(),
                currency,
                "ACTIVE",
                request.region(),
                request.startsAt(),
                request.endsAt()
        );
        Listing saved = listingRepository.save(listing);
        return toResponse(saved);
    }

    @Transactional
    public ListingResponse updateListing(Long id, UpdateListingRequest request) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + id));

        if (request.quantityAvailable() != null) {
            BigDecimal available = inventoryService.availableQuantity(
                    listing.getSellerType(), listing.getSellerId(), listing.getProductId());
            if (available.compareTo(request.quantityAvailable()) < 0) {
                throw new BusinessRuleException(
                        "Updated quantity (" + request.quantityAvailable()
                                + ") exceeds available inventory (" + available + ")");
            }
        }

        listing.update(request.quantityAvailable(), request.pricePerUnit(), request.region());
        Listing updated = listingRepository.save(listing);
        return toResponse(updated);
    }

    @Transactional
    public ListingResponse pauseListing(Long id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + id));
        ListingStateMachine.Status current = ListingStateMachine.Status.valueOf(listing.getStatus());
        ListingStateMachine.assertTransition(current, ListingStateMachine.Status.PAUSED);

        listing.setStatus("PAUSED");
        return toResponse(listingRepository.save(listing));
    }

    @Transactional
    public ListingResponse activateListing(Long id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + id));
        ListingStateMachine.Status current = ListingStateMachine.Status.valueOf(listing.getStatus());
        ListingStateMachine.assertTransition(current, ListingStateMachine.Status.ACTIVE);

        listing.setStatus("ACTIVE");
        return toResponse(listingRepository.save(listing));
    }

    private ListingResponse toResponse(Listing listing) {
        String productName = productRepository.findById(listing.getProductId())
                .map(Product::getName)
                .orElse("Unknown Product");
        return ListingResponse.of(listing, productName);
    }
}

