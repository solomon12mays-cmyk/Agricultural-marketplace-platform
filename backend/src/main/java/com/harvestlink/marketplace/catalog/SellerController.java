package com.harvestlink.marketplace.catalog;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
@Transactional(readOnly = true)
public class SellerController {

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;

    public SellerController(SellerRepository sellerRepository, ProductRepository productRepository) {
        this.sellerRepository = sellerRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/sellers")
    public List<SellerSummaryResponse> getSellers() {
        return sellerRepository.findAllByOrderByNameAsc().stream()
                .map(SellerSummaryResponse::from)
                .toList();
    }

    @GetMapping("/sellers/{slug}")
    public SellerDetailResponse getSeller(@PathVariable String slug) {
        return SellerDetailResponse.from(findSellerBySlug(slug));
    }

    @GetMapping("/sellers/{slug}/inventory")
    public List<InventoryItemResponse> getSellerInventory(@PathVariable String slug) {
        Seller seller = findSellerBySlug(slug);
        return seller.getProducts().stream()
                .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                .map(InventoryItemResponse::from)
                .toList();
    }

    @PatchMapping("/sellers/{slug}/inventory/{productSlug}")
    @Transactional
    public InventoryItemResponse updateSellerInventory(@PathVariable String slug,
                                                      @PathVariable String productSlug,
                                                      @RequestBody InventoryUpdateRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inventory update payload is required");
        }

        Seller seller = findSellerBySlug(slug);
        Product product = seller.getProducts().stream()
                .filter(item -> item.getSlug().equalsIgnoreCase(productSlug))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory item not found"));

        Integer requestedQuantity = request.stockQuantity();
        Integer delta = request.delta();
        boolean hasRequestedQuantity = requestedQuantity != null;
        boolean hasDelta = delta != null;

        if (!hasRequestedQuantity && !hasDelta) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either stockQuantity or delta is required");
        }

        if (hasRequestedQuantity && hasDelta) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide either stockQuantity or delta, not both");
        }

        if (hasRequestedQuantity) {
            if (requestedQuantity < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock quantity cannot be negative");
            }
            product.setStockQuantity(requestedQuantity);
        } else {
            int nextQuantity = Math.max(0, product.getStockQuantity() + delta);
            product.setStockQuantity(nextQuantity);
        }

        product.refreshStatusFromStock();

        productRepository.save(product);
        return InventoryItemResponse.from(product);
    }

    private Seller findSellerBySlug(String slug) {
        return sellerRepository.findAllByOrderByNameAsc().stream()
                .filter(seller -> seller.getSlug().equalsIgnoreCase(slug))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
    }

    public record SellerSummaryResponse(Long id, String name, String slug, String location,
                                        String description, Double rating, Integer productCount) {
        public static SellerSummaryResponse from(Seller seller) {
            return new SellerSummaryResponse(
                    seller.getId(),
                    seller.getName(),
                    seller.getSlug(),
                    seller.getLocation(),
                    seller.getDescription(),
                    seller.getRating(),
                    seller.getProducts() != null ? seller.getProducts().size() : 0);
        }
    }

    public record SellerDetailResponse(Long id, String name, String slug, String location,
                                       String description, Double rating, Integer productCount,
                                       List<SellerProductSummary> products) {
        public static SellerDetailResponse from(Seller seller) {
            List<SellerProductSummary> products = seller.getProducts().stream()
                    .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                    .map(SellerProductSummary::from)
                    .toList();

            return new SellerDetailResponse(
                    seller.getId(),
                    seller.getName(),
                    seller.getSlug(),
                    seller.getLocation(),
                    seller.getDescription(),
                    seller.getRating(),
                    products.size(),
                    products);
        }
    }

    public record SellerProductSummary(Long id, String name, String slug, String unit,
                                      BigDecimal price, String currency, String categoryName) {
        public static SellerProductSummary from(Product product) {
            return new SellerProductSummary(
                    product.getId(),
                    product.getName(),
                    product.getSlug(),
                    product.getUnit(),
                    product.getPrice(),
                    product.getCurrency(),
                    product.getCategory() != null ? product.getCategory().getName() : null);
        }
    }

    public record InventoryItemResponse(Long id, String name, String slug, String unit,
                                       BigDecimal price, String currency, Integer stockQuantity,
                                       String status, Boolean lowStock, String categoryName) {
        public static InventoryItemResponse from(Product product) {
            return new InventoryItemResponse(
                    product.getId(),
                    product.getName(),
                    product.getSlug(),
                    product.getUnit(),
                    product.getPrice(),
                    product.getCurrency(),
                    product.getStockQuantity(),
                    product.getStatus() != null ? product.getStatus().name() : null,
                    product.getStockQuantity() != null && product.getStockQuantity() <= 15,
                    product.getCategory() != null ? product.getCategory().getName() : null);
        }
    }

    public record InventoryUpdateRequest(Integer stockQuantity, Integer delta) {
    }

    @PatchMapping("/sellers/{slug}/profile")
    @Transactional
    public SellerSummaryResponse updateSellerProfile(@PathVariable String slug,
                                                   @RequestBody SellerProfileUpdateRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seller profile update payload is required");
        }

        Seller seller = findSellerBySlug(slug);

        if (StringUtils.hasText(request.name())) {
            seller.setName(request.name());
        }
        if (StringUtils.hasText(request.location())) {
            seller.setLocation(request.location());
        }
        if (StringUtils.hasText(request.description())) {
            seller.setDescription(request.description());
        }
        if (request.rating() != null) {
            if (request.rating() < 0 || request.rating() > 5) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seller rating must be between 0 and 5");
            }
            seller.setRating(request.rating());
        }

        return SellerSummaryResponse.from(sellerRepository.save(seller));
    }

    public record SellerProfileUpdateRequest(String name, String location, String description, Double rating) {
    }
}
