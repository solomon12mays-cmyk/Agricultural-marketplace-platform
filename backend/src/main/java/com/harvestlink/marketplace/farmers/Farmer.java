package com.harvestlink.marketplace.farmers;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "farmers", schema = "marketplace",
        indexes = {@Index(name = "idx_farmers_slug", columnList = "slug", unique = true)})
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String slug;

    @Column(nullable = false, length = 120)
    private String region;

    @Column(nullable = false, length = 120)
    private String cropFocus;

    @Column(nullable = false, length = 80)
    private String farmSize;

    @Column(nullable = false)
    private Double rating = 4.8;

    @Column(length = 500)
    private String bio;

    @Column(nullable = false, length = 160)
    private String contactName;

    @Column(nullable = false, length = 40)
    private String contactPhone;

    @Column(nullable = false)
    private Integer harvestsThisSeason = 0;

    protected Farmer() {
    }

    public Farmer(String name, String slug, String region, String cropFocus,
                  String farmSize, Double rating, String bio,
                  String contactName, String contactPhone, Integer harvestsThisSeason) {
        this.name = name;
        this.slug = slug;
        this.region = region;
        this.cropFocus = cropFocus;
        this.farmSize = farmSize;
        this.rating = rating;
        this.bio = bio;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.harvestsThisSeason = harvestsThisSeason;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCropFocus() {
        return cropFocus;
    }

    public void setCropFocus(String cropFocus) {
        this.cropFocus = cropFocus;
    }

    public String getFarmSize() {
        return farmSize;
    }

    public void setFarmSize(String farmSize) {
        this.farmSize = farmSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Integer getHarvestsThisSeason() {
        return harvestsThisSeason;
    }

    public void setHarvestsThisSeason(Integer harvestsThisSeason) {
        this.harvestsThisSeason = harvestsThisSeason;
    }
}
