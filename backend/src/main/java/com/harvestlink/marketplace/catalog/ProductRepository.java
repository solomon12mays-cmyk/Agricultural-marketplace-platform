package com.harvestlink.marketplace.catalog;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByNameAsc();

    List<Product> findAllByOrderByPriceAsc();

    List<Product> findAllByOrderByPriceDesc();

    List<Product> findByCategorySlugOrderByNameAsc(String categorySlug);

    List<Product> findByCategorySlugOrderByPriceAsc(String categorySlug);

    List<Product> findByCategorySlugOrderByPriceDesc(String categorySlug);

    List<Product> findByCategorySlugAndNameContainingIgnoreCaseOrderByNameAsc(String categorySlug, String term);

    List<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String term);

    List<Product> findByNameContainingIgnoreCaseOrderByPriceAsc(String term);

    List<Product> findByNameContainingIgnoreCaseOrderByPriceDesc(String term);

    List<Product> findByCategorySlugAndNameContainingIgnoreCaseOrderByPriceAsc(String categorySlug, String term);

    List<Product> findByCategorySlugAndNameContainingIgnoreCaseOrderByPriceDesc(String categorySlug, String term);

    Optional<Product> findBySlug(String slug);
}
