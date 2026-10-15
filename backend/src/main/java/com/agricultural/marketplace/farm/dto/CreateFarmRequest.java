package com.agricultural.marketplace.farm.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateFarmRequest(
        @NotNull Long farmerId,
        @NotBlank @Size(max = 128) String name,
        @Size(max = 64) String farmType,
        BigDecimal sizeHectares,
        @Size(max = 32) String certificationStatus,
        CreateFarmLocationRequest location) {
}

