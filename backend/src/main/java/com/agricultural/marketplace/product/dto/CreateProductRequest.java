package com.agricultural.marketplace.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
        @NotBlank @Size(max = 128) String name,
        @NotBlank @Size(max = 128) String slug,
        @Size(max = 2000) String description,
        @Size(max = 64) String category,
        @NotBlank @Size(max = 16) String unit) {
}

