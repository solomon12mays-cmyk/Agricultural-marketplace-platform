package com.agricultural.marketplace.admin.dto;

import java.math.BigDecimal;

public record AdminDashboardResponse(
        long totalOrders,
        long activeListings,
        long registeredFarmers,
        long registeredDrivers,
        long verifiedOrganizations,
        long openComplianceCases,
        long openDisputes,
        BigDecimal totalGmv
) {
}

