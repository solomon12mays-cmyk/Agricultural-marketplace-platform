package com.agricultural.marketplace.product.repository;

import java.util.List;
import java.util.Optional;

import com.agricultural.marketplace.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySlug(String slug);

    List<Product> findByStatus(String status);

    List<Product> findByCategoryIgnoreCase(String category);
}

