package com.agricultural.marketplace.farmer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateFarmerProfileRequest(
        @NotBlank @Size(max = 128) String fullName,
        @NotBlank @Size(max = 64) String nationalId,
        @NotBlank @Size(max = 32) String phone,
        @Size(max = 64) String region,
        @Size(max = 64) String zone,
        @Size(max = 64) String woreda) {
}

