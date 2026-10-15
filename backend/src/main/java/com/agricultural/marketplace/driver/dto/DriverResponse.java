package com.agricultural.marketplace.driver.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.agricultural.marketplace.driver.entity.Driver;

public record DriverResponse(
        Long id,
        Long userId,
        String fullName,
        String licenseNumber,
        LocalDateTime licenseExpiresAt,
        String phone,
        String status,
        BigDecimal rating,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static DriverResponse fromEntity(Driver driver) {
        return new DriverResponse(
                driver.getId(),
                driver.getUserId(),
                driver.getFullName(),
                driver.getLicenseNumber(),
                driver.getLicenseExpiresAt(),
                driver.getPhone(),
                driver.getStatus(),
                driver.getRating(),
                driver.getCreatedAt(),
                driver.getUpdatedAt()
        );
    }
}

