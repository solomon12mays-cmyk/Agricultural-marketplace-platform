package com.harvestlink.marketplace.catalog;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
@Transactional(readOnly = true)
public class CatalogController {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public CatalogController(ProductRepository productRepository,
                            ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @GetMapping("/catalog")
    public CatalogResponse getCatalog(@RequestParam(name = "category", required = false) String categorySlug,
                                    @RequestParam(name = "search", required = false) String search,
                                    @RequestParam(name = "sort", defaultValue = "name_asc") String sort) {
        List<CategorySummary> categories = productCategoryRepository.findAllByOrderByNameAsc().stream()
                .map(CategorySummary::from)
                .toList();

        String normalizedSearch = search == null ? "" : search.trim();
        String normalizedCategory = categorySlug == null ? "" : categorySlug.trim();

        List<Product> products = resolveProducts(normalizedCategory, normalizedSearch, sort);

        return new CatalogResponse(categories, products.stream().map(ProductSummary::from).toList());
    }

    private List<Product> resolveProducts(String categorySlug, String searchTerm, String sort) {
        boolean hasCategory = !categorySlug.isBlank();
        boolean hasSearch = !searchTerm.isBlank();

        if (hasCategory && hasSearch) {
            return switch (sort) {
                case "price_asc" -> productRepository.findByCategorySlugAndNameContainingIgnoreCaseOrderByPriceAsc(categorySlug, searchTerm);
                case "price_desc" -> productRepository.findByCategorySlugAndNameContainingIgnoreCaseOrderByPriceDesc(categorySlug, searchTerm);
                default -> productRepository.findByCategorySlugAndNameContainingIgnoreCaseOrderByNameAsc(categorySlug, searchTerm);
            };
        }

        if (hasCategory) {
            return switch (sort) {
                case "price_asc" -> productRepository.findByCategorySlugOrderByPriceAsc(categorySlug);
                case "price_desc" -> productRepository.findByCategorySlugOrderByPriceDesc(categorySlug);
                default -> productRepository.findByCategorySlugOrderByNameAsc(categorySlug);
            };
        }

        if (hasSearch) {
            return switch (sort) {
                case "price_asc" -> productRepository.findByNameContainingIgnoreCaseOrderByPriceAsc(searchTerm);
                case "price_desc" -> productRepository.findByNameContainingIgnoreCaseOrderByPriceDesc(searchTerm);
                default -> productRepository.findByNameContainingIgnoreCaseOrderByNameAsc(searchTerm);
            };
        }

        return switch (sort) {
            case "price_asc" -> productRepository.findAllByOrderByPriceAsc();
            case "price_desc" -> productRepository.findAllByOrderByPriceDesc();
            default -> productRepository.findAllByOrderByNameAsc();
        };
    }

    @GetMapping("/catalog/products/{slug}")
    public ProductDetailResponse getProduct(@PathVariable String slug) {
        return productRepository.findBySlug(slug)
                .map(ProductDetailResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    public record CatalogResponse(List<CategorySummary> categories, List<ProductSummary> products) {
    }

    public record CategorySummary(Long id, String name, String slug, String description) {
        public static CategorySummary from(ProductCategory category) {
            return new CategorySummary(category.getId(), category.getName(), category.getSlug(), category.getDescription());
        }
    }

    public record ProductSummary(Long id, String name, String slug, String description, String origin,
                                String unit, BigDecimal price, String currency, Integer stockQuantity,
                                String categoryName, String imageUrl, String sellerName, String sellerLocation) {
        public static ProductSummary from(Product product) {
            Seller seller = product.getSeller();
            return new ProductSummary(
                    product.getId(),
                    product.getName(),
                    product.getSlug(),
                    product.getDescription(),
                    product.getOrigin(),
                    product.getUnit(),
                    product.getPrice(),
                    product.getCurrency(),
                    product.getStockQuantity(),
                    product.getCategory() != null ? product.getCategory().getName() : null,
                    product.getImageUrl(),
                    seller != null ? seller.getName() : null,
                    seller != null ? seller.getLocation() : null);
        }
    }

    public record ProductDetailResponse(Long id, String name, String slug, String description, String origin,
                                       String unit, BigDecimal price, String currency, Integer stockQuantity,
                                       String categoryName, String categorySlug, String status, String imageUrl,
                                       String sellerName, String sellerLocation, Double sellerRating) {
        public static ProductDetailResponse from(Product product) {
            ProductCategory category = product.getCategory();
            Seller seller = product.getSeller();
            return new ProductDetailResponse(
                    product.getId(),
                    product.getName(),
                    product.getSlug(),
                    product.getDescription(),
                    product.getOrigin(),
                    product.getUnit(),
                    product.getPrice(),
                    product.getCurrency(),
                    product.getStockQuantity(),
                    category != null ? category.getName() : null,
                    category != null ? category.getSlug() : null,
                    product.getStatus() != null ? product.getStatus().name() : null,
                    product.getImageUrl(),
                    seller != null ? seller.getName() : null,
                    seller != null ? seller.getLocation() : null,
                    seller != null ? seller.getRating() : null);
        }
    }
}
