package com.agricultural.marketplace.marketplace.search;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Ranks search results using configurable deterministic weights.
 * Design allows future ML ranking adapters to replace or augment scoring.
 */
@Service
public class MarketplaceRankingService {

    @Value("${app.marketplace.ranking.quantity-weight:0.25}")
    private double quantityWeight;

    @Value("${app.marketplace.ranking.price-weight:0.25}")
    private double priceWeight;

    @Value("${app.marketplace.ranking.freshness-weight:0.25}")
    private double freshnessWeight;

    @Value("${app.marketplace.ranking.region-weight:0.25}")
    private double regionWeight;

    public double score(BigDecimal quantity, BigDecimal price, String listingRegion, String searchRegion, LocalDateTime listedAt) {
        double score = 0.0;

        // Quantity signal (normalized up to 10,000 units)
        if (quantity != null) {
            double qScore = Math.min(1.0, quantity.doubleValue() / 10000.0);
            score += qScore * quantityWeight;
        }

        // Price signal (favorable for reasonable/lower price per unit)
        if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
            double pScore = Math.max(0.1, 100.0 / (100.0 + price.doubleValue()));
            score += pScore * priceWeight;
        }

        // Location match signal
        if (searchRegion != null && listingRegion != null && searchRegion.equalsIgnoreCase(listingRegion)) {
            score += 1.0 * regionWeight;
        }

        // Freshness signal (decay over 30 days)
        if (listedAt != null) {
            long daysOld = Duration.between(listedAt, LocalDateTime.now()).toDays();
            double fScore = Math.max(0.0, 1.0 - (daysOld / 30.0));
            score += fScore * freshnessWeight;
        }

        return Math.round(score * 100.0) / 100.0;
    }
}
