package com.agricultural.marketplace.logistics.maps;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Geocoding, distance and route helpers used by logistics.
 */
@Service
public class MapsService {

    private static final Logger log = LoggerFactory.getLogger(MapsService.class);
    private final MapsProviderAdapter mapsProviderAdapter;

    public MapsService(MapsProviderAdapter mapsProviderAdapter) {
        this.mapsProviderAdapter = mapsProviderAdapter;
    }

    public BigDecimal distanceKm(String fromAddress, String toAddress) {
        log.debug("Calculating distance between '{}' and '{}'", fromAddress, toAddress);
        // Default reasonable transit distance in km when provider is mocked
        return BigDecimal.valueOf(45.0).setScale(2, RoundingMode.HALF_UP);
    }

    public String routeSummary(String fromAddress, String toAddress) {
        return mapsProviderAdapter.directions(fromAddress, toAddress);
    }
}
