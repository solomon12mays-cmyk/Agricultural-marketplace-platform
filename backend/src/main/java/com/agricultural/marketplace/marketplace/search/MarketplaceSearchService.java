package com.agricultural.marketplace.marketplace.search;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.agricultural.marketplace.listing.entity.Listing;
import com.agricultural.marketplace.listing.repository.ListingRepository;
import com.agricultural.marketplace.marketplace.dto.MarketplaceItemResponse;
import com.agricultural.marketplace.product.entity.Product;
import com.agricultural.marketplace.product.repository.ProductRepository;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import com.agricultural.marketplace.shared.response.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MarketplaceSearchService {

    private final ListingRepository listingRepository;
    private final ProductRepository productRepository;
    private final MarketplaceRankingService rankingService;

    public MarketplaceSearchService(ListingRepository listingRepository,
                                    ProductRepository productRepository,
                                    MarketplaceRankingService rankingService) {
        this.listingRepository = listingRepository;
        this.productRepository = productRepository;
        this.rankingService = rankingService;
    }

    public PageResponse<MarketplaceItemResponse> search(
            String keyword, String region, String category,
            BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {

        List<Listing> activeListings = listingRepository.findByStatus("ACTIVE");
        Map<Long, Product> productMap = productRepository.findAll().stream()
                .collect(Collectors.toMap(Product::getId, Function.identity(), (a, b) -> a));

        List<MarketplaceItemResponse> matched = activeListings.stream()
                .filter(l -> {
                    Product p = productMap.get(l.getProductId());
                    if (p == null) return false;

                    if (keyword != null && !keyword.isBlank()) {
                        String kw = keyword.toLowerCase();
                        boolean matchName = p.getName() != null && p.getName().toLowerCase().contains(kw);
                        boolean matchDesc = p.getDescription() != null && p.getDescription().toLowerCase().contains(kw);
                        if (!matchName && !matchDesc) return false;
                    }

                    if (region != null && !region.isBlank()) {
                        if (l.getRegion() == null || !l.getRegion().equalsIgnoreCase(region)) {
                            return false;
                        }
                    }

                    if (category != null && !category.isBlank()) {
                        if (p.getCategory() == null || !p.getCategory().equalsIgnoreCase(category)) {
                            return false;
                        }
                    }

                    if (minPrice != null && l.getPricePerUnit().compareTo(minPrice) < 0) {
                        return false;
                    }

                    if (maxPrice != null && l.getPricePerUnit().compareTo(maxPrice) > 0) {
                        return false;
                    }

                    return true;
                })
                .map(l -> {
                    Product p = productMap.get(l.getProductId());
                    String pName = p != null ? p.getName() : "Produce";
                    String pCat = p != null ? p.getCategory() : "General";
                    double score = rankingService.score(
                            l.getQuantityAvailable(),
                            l.getPricePerUnit(),
                            l.getRegion(),
                            region,
                            l.getCreatedAt());

                    return new MarketplaceItemResponse(
                            l.getId(),
                            l.getSellerType(),
                            l.getSellerId(),
                            l.getProductId(),
                            pName,
                            pCat,
                            l.getQuantityAvailable(),
                            l.getUnit(),
                            l.getPricePerUnit(),
                            l.getCurrency(),
                            l.getRegion(),
                            l.getStatus(),
                            score,
                            l.getCreatedAt());
                })
                .sorted(Comparator.comparingDouble(MarketplaceItemResponse::relevanceScore).reversed())
                .toList();

        int safePage = Math.max(0, page);
        int safeSize = size > 0 ? size : 20;
        int fromIndex = safePage * safeSize;
        int toIndex = Math.min(fromIndex + safeSize, matched.size());

        List<MarketplaceItemResponse> content = fromIndex < matched.size()
                ? matched.subList(fromIndex, toIndex)
                : List.of();

        return PageResponse.of(content, safePage, safeSize, matched.size());
    }

    public MarketplaceItemResponse getListingDetails(Long listingId) {
        Listing l = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + listingId));
        Product p = productRepository.findById(l.getProductId()).orElse(null);

        return new MarketplaceItemResponse(
                l.getId(),
                l.getSellerType(),
                l.getSellerId(),
                l.getProductId(),
                p != null ? p.getName() : "Produce",
                p != null ? p.getCategory() : "General",
                l.getQuantityAvailable(),
                l.getUnit(),
                l.getPricePerUnit(),
                l.getCurrency(),
                l.getRegion(),
                l.getStatus(),
                1.0,
                l.getCreatedAt());
    }
}
