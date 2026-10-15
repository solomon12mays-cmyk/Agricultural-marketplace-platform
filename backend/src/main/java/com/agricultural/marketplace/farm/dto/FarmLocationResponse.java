package com.agricultural.marketplace.farm.dto;

import java.math.BigDecimal;

import com.agricultural.marketplace.farm.entity.FarmLocation;

public record FarmLocationResponse(
        Long id,
        Long farmId,
        String region,
        String zone,
        String woreda,
        String village,
        BigDecimal latitude,
        BigDecimal longitude) {

    public static FarmLocationResponse from(FarmLocation entity) {
        return new FarmLocationResponse(
                entity.getId(),
                entity.getFarmId(),
                entity.getRegion(),
                entity.getZone(),
                entity.getWoreda(),
                entity.getVillage(),
                entity.getLatitude(),
                entity.getLongitude());
    }
}

