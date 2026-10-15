package com.agricultural.marketplace.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.agricultural.marketplace.product.entity.GrainVariety;

public record GrainVarietyResponse(
        Long id,
        String name,
        String cropType,
        String region,
        Integer maturityDays,
        BigDecimal proteinPercent,
        String notes,
        LocalDateTime createdAt) {

    public static GrainVarietyResponse from(GrainVariety entity) {
        return new GrainVarietyResponse(
                entity.getId(),
                entity.getName(),
                entity.getCropType(),
                entity.getRegion(),
                entity.getMaturityDays(),
                entity.getProteinPercent(),
                entity.getNotes(),
                entity.getCreatedAt());
    }
}

