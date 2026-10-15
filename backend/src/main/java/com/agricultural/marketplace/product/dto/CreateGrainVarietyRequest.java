package com.agricultural.marketplace.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateGrainVarietyRequest(
        @NotBlank @Size(max = 128) String name,
        @NotBlank @Size(max = 64) String cropType,
        @Size(max = 64) String region,
        Integer maturityDays,
        BigDecimal proteinPercent,
        @Size(max = 500) String notes) {
}

