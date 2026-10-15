package com.agricultural.marketplace.farm.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Size;

public record CreateFarmLocationRequest(
        @Size(max = 64) String region,
        @Size(max = 64) String zone,
        @Size(max = 64) String woreda,
        @Size(max = 128) String village,
        BigDecimal latitude,
        BigDecimal longitude) {
}

