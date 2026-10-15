package com.agricultural.marketplace.logistics.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

/**
 * Prices transport jobs.
 */
@Service
public class TransportPricingService {

    private static final BigDecimal BASE_FARE = new BigDecimal("350.00");
    private static final BigDecimal RATE_PER_KM = new BigDecimal("18.50");
    private static final BigDecimal RATE_PER_100KG = new BigDecimal("5.00");

    public BigDecimal quote(BigDecimal distanceKm, String vehicleType, BigDecimal capacityKg) {
        BigDecimal distance = distanceKm != null ? distanceKm : new BigDecimal("30.00");
        BigDecimal capacity = capacityKg != null ? capacityKg : BigDecimal.ZERO;

        BigDecimal distanceCost = distance.multiply(RATE_PER_KM);
        BigDecimal weightCost = capacity.divide(new BigDecimal("100.00"), 4, RoundingMode.HALF_UP)
                .multiply(RATE_PER_100KG);

        return BASE_FARE.add(distanceCost).add(weightCost).setScale(2, RoundingMode.HALF_UP);
    }
}
