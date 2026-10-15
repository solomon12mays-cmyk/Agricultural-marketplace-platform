package com.agricultural.marketplace.order.service;

import java.util.List;

import com.agricultural.marketplace.inventory.service.InventoryReservationService;
import com.agricultural.marketplace.listing.entity.Listing;
import com.agricultural.marketplace.listing.repository.ListingRepository;
import com.agricultural.marketplace.order.entity.OrderItem;
import com.agricultural.marketplace.order.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderReservationService {

    private final OrderItemRepository orderItemRepository;
    private final ListingRepository listingRepository;
    private final InventoryReservationService inventoryReservationService;

    public OrderReservationService(OrderItemRepository orderItemRepository,
                                   ListingRepository listingRepository,
                                   InventoryReservationService inventoryReservationService) {
        this.orderItemRepository = orderItemRepository;
        this.listingRepository = listingRepository;
        this.inventoryReservationService = inventoryReservationService;
    }

    @Transactional
    public void reserveStock(Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : items) {
            Listing listing = listingRepository.findById(item.getListingId())
                    .orElseThrow(() -> new IllegalStateException("Listing not found: " + item.getListingId()));
            inventoryReservationService.reserve(
                    item.getProductId(),
                    listing.getSellerType(),
                    listing.getSellerId(),
                    item.getQuantity(),
                    orderId.toString()
            );
        }
    }

    @Transactional
    public void releaseStock(Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : items) {
            listingRepository.findById(item.getListingId())
                    .ifPresent(listing -> inventoryReservationService.release(
                            item.getProductId(),
                            listing.getSellerType(),
                            listing.getSellerId(),
                            item.getQuantity(),
                            orderId.toString()
                    ));
        }
    }

    @Transactional
    public void consumeStock(Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : items) {
            listingRepository.findById(item.getListingId())
                    .ifPresent(listing -> inventoryReservationService.consume(
                            item.getProductId(),
                            listing.getSellerType(),
                            listing.getSellerId(),
                            item.getQuantity(),
                            orderId.toString()
                    ));
        }
    }
}
