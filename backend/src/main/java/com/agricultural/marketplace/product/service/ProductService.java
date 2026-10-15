package com.agricultural.marketplace.product.service;

import java.util.List;

import com.agricultural.marketplace.product.dto.CreateGrainVarietyRequest;
import com.agricultural.marketplace.product.dto.CreateProductRequest;
import com.agricultural.marketplace.product.dto.GrainVarietyResponse;
import com.agricultural.marketplace.product.dto.ProductResponse;
import com.agricultural.marketplace.product.entity.GrainVariety;
import com.agricultural.marketplace.product.entity.Product;
import com.agricultural.marketplace.product.repository.GrainVarietyRepository;
import com.agricultural.marketplace.product.repository.ProductRepository;
import com.agricultural.marketplace.shared.exception.ConflictException;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final GrainVarietyRepository grainVarietyRepository;

    public ProductService(ProductRepository productRepository,
                          GrainVarietyRepository grainVarietyRepository) {
        this.productRepository = productRepository;
        this.grainVarietyRepository = grainVarietyRepository;
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    public List<ProductResponse> getActiveProducts() {
        return productRepository.findByStatus("ACTIVE").stream()
                .map(ProductResponse::from)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return ProductResponse.from(product);
    }

    public ProductResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with slug: " + slug));
        return ProductResponse.from(product);
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request, String createdBy) {
        if (productRepository.findBySlug(request.slug()).isPresent()) {
            throw new ConflictException("Product already exists with slug: " + request.slug());
        }

        Product product = new Product(
                request.name(),
                request.slug(),
                request.description(),
                request.category(),
                request.unit(),
                "ACTIVE",
                createdBy
        );
        Product saved = productRepository.save(product);
        return ProductResponse.from(saved);
    }

    public List<GrainVarietyResponse> getAllVarieties() {
        return grainVarietyRepository.findAll().stream()
                .map(GrainVarietyResponse::from)
                .toList();
    }

    public List<GrainVarietyResponse> getVarietiesByCropType(String cropType) {
        return grainVarietyRepository.findByCropTypeIgnoreCase(cropType).stream()
                .map(GrainVarietyResponse::from)
                .toList();
    }

    @Transactional
    public GrainVarietyResponse createVariety(CreateGrainVarietyRequest request) {
        GrainVariety variety = new GrainVariety(
                request.name(),
                request.cropType(),
                request.region(),
                request.maturityDays(),
                request.proteinPercent(),
                request.notes()
        );
        GrainVariety saved = grainVarietyRepository.save(variety);
        return GrainVarietyResponse.from(saved);
    }
}

