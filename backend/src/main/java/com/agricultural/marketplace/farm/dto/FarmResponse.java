package com.agricultural.marketplace.farm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.agricultural.marketplace.farm.entity.Farm;
import com.agricultural.marketplace.farm.entity.FarmLocation;

public record FarmResponse(
        Long id,
        Long farmerId,
        String name,
        String farmType,
        BigDecimal sizeHectares,
        String certificationStatus,
        FarmLocationResponse location,
        LocalDateTime createdAt) {

    public static FarmResponse of(Farm farm, FarmLocation location) {
        return new FarmResponse(
                farm.getId(),
                farm.getFarmerId(),
                farm.getName(),
                farm.getFarmType(),
                farm.getSizeHectares(),
                farm.getCertificationStatus(),
                location != null ? FarmLocationResponse.from(location) : null,
                farm.getCreatedAt());
    }
}

