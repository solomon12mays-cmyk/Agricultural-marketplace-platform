package com.agricultural.marketplace.farm.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Administrative and geographic location of a farm.
 */
@Entity
@Table(name = "farm_locations")
public class FarmLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "farm_id", nullable = false)
    private Long farmId;

    @Column(name = "region", length = 64)
    private String region;

    @Column(name = "zone", length = 64)
    private String zone;

    @Column(name = "woreda", length = 64)
    private String woreda;

    @Column(name = "village", length = 128)
    private String village;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    protected FarmLocation() {
        // for JPA
    }

    public FarmLocation(Long farmId, String region, String zone, String woreda) {
        this(farmId, region, zone, woreda, null, null, null);
    }

    public FarmLocation(Long farmId, String region, String zone, String woreda, String village, BigDecimal latitude, BigDecimal longitude) {
        this.farmId = farmId;
        this.region = region;
        this.zone = zone;
        this.woreda = woreda;
        this.village = village;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public Long getFarmId() {
        return farmId;
    }

    public String getRegion() {
        return region;
    }

    public String getZone() {
        return zone;
    }

    public String getWoreda() {
        return woreda;
    }

    public String getVillage() {
        return village;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }
}
