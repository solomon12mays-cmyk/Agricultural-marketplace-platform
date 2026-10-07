package com.harvestlink.marketplace.catalog;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "sellers", schema = "marketplace",
        indexes = {@Index(name = "idx_sellers_slug", columnList = "slug", unique = true)})
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String slug;

    @Column(nullable = false, length = 160)
    private String location;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Double rating = 4.8;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    protected Seller() {
    }

    public Seller(String name, String slug, String location, String description, Double rating) {
        this.name = name;
        this.slug = slug;
        this.location = location;
        this.description = description;
        this.rating = rating;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<Product> getProducts() {
        return products;
    }
}
