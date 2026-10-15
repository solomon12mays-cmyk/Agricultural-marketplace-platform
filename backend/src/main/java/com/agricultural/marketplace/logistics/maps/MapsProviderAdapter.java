package com.agricultural.marketplace.logistics.maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Adapter over a concrete maps provider (Google Maps, Mapbox, OSRM, etc.).
 */
@Component
public class MapsProviderAdapter {

    private static final Logger log = LoggerFactory.getLogger(MapsProviderAdapter.class);

    public String geocode(String address) {
        log.debug("Geocoding address: {}", address);
        // Default coordinates for Addis Ababa / central Ethiopian hub if external provider not configured
        return "9.010793,38.761252";
    }

    public String directions(String fromAddress, String toAddress) {
        log.debug("Querying directions from '{}' to '{}'", fromAddress, toAddress);
        return "Route via National Highway A1";
    }
}
