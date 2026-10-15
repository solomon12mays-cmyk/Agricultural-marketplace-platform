package com.agricultural.marketplace.admin.service;

import java.math.BigDecimal;

import com.agricultural.marketplace.admin.dto.AdminDashboardResponse;
import com.agricultural.marketplace.compliance.repository.ComplianceCaseRepository;
import com.agricultural.marketplace.compliance.repository.DisputeRepository;
import com.agricultural.marketplace.driver.repository.DriverRepository;
import com.agricultural.marketplace.farmer.repository.FarmerRepository;
import com.agricultural.marketplace.listing.repository.ListingRepository;
import com.agricultural.marketplace.order.entity.Order;
import com.agricultural.marketplace.order.repository.OrderRepository;
import com.agricultural.marketplace.organization.repository.OrganizationRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminService {

    private final OrderRepository orderRepository;
    private final ListingRepository listingRepository;
    private final FarmerRepository farmerRepository;
    private final DriverRepository driverRepository;
    private final OrganizationRepository organizationRepository;
    private final ComplianceCaseRepository complianceCaseRepository;
    private final DisputeRepository disputeRepository;

    public AdminService(OrderRepository orderRepository,
                        ListingRepository listingRepository,
                        FarmerRepository farmerRepository,
                        DriverRepository driverRepository,
                        OrganizationRepository organizationRepository,
                        ComplianceCaseRepository complianceCaseRepository,
                        DisputeRepository disputeRepository) {
        this.orderRepository = orderRepository;
        this.listingRepository = listingRepository;
        this.farmerRepository = farmerRepository;
        this.driverRepository = driverRepository;
        this.organizationRepository = organizationRepository;
        this.complianceCaseRepository = complianceCaseRepository;
        this.disputeRepository = disputeRepository;
    }

    public AdminDashboardResponse getDashboard() {
        long totalOrders = orderRepository.count();
        long activeListings = listingRepository.findByStatus("ACTIVE").size();
        long registeredFarmers = farmerRepository.count();
        long registeredDrivers = driverRepository.count();
        long verifiedOrganizations = organizationRepository.findByVerificationStatus("VERIFIED", Pageable.unpaged()).getTotalElements();
        long openComplianceCases = complianceCaseRepository.findByStatus("OPEN", Pageable.unpaged()).getTotalElements();
        long openDisputes = disputeRepository.findByStatus("OPEN", Pageable.unpaged()).getTotalElements();

        BigDecimal totalGmv = orderRepository.findAll().stream()
                .filter(o -> !"CANCELLED".equalsIgnoreCase(o.getStatus()))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AdminDashboardResponse(
                totalOrders,
                activeListings,
                registeredFarmers,
                registeredDrivers,
                verifiedOrganizations,
                openComplianceCases,
                openDisputes,
                totalGmv
        );
    }
}
