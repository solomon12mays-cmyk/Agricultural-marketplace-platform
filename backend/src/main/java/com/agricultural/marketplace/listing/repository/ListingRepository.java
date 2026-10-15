package com.agricultural.marketplace.listing.repository;

import java.util.List;

import com.agricultural.marketplace.listing.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {

    List<Listing> findBySellerTypeAndSellerId(String sellerType, Long sellerId);

    List<Listing> findByStatus(String status);

    List<Listing> findByProductIdAndStatus(Long productId, String status);

    List<Listing> findByStatusAndRegionIgnoreCase(String status, String region);
}

