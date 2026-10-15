package com.agricultural.marketplace.farmer.dto;

import jakarta.validation.constraints.Size;

public record UpdateFarmerProfileRequest(
        @Size(max = 128) String fullName,
        @Size(max = 64) String region,
        @Size(max = 64) String zone,
        @Size(max = 64) String woreda) {
}

