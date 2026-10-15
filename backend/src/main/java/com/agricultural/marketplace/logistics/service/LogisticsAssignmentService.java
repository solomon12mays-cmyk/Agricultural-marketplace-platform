package com.agricultural.marketplace.logistics.service;

import java.math.BigDecimal;
import java.util.List;

import com.agricultural.marketplace.driver.entity.Vehicle;
import com.agricultural.marketplace.driver.repository.DriverRepository;
import com.agricultural.marketplace.driver.repository.VehicleRepository;
import com.agricultural.marketplace.logistics.dto.AssignDriverRequest;
import com.agricultural.marketplace.logistics.dto.CreateLogisticsRequest;
import com.agricultural.marketplace.logistics.dto.LogisticsAssignmentResponse;
import com.agricultural.marketplace.logistics.entity.LogisticsAssignment;
import com.agricultural.marketplace.logistics.maps.MapsService;
import com.agricultural.marketplace.logistics.repository.LogisticsAssignmentRepository;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogisticsAssignmentService {

    private final LogisticsAssignmentRepository logisticsAssignmentRepository;
    private final DriverMatchingService driverMatchingService;
    private final TransportPricingService transportPricingService;
    private final MapsService mapsService;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public LogisticsAssignmentService(LogisticsAssignmentRepository logisticsAssignmentRepository,
                                      DriverMatchingService driverMatchingService,
                                      TransportPricingService transportPricingService,
                                      MapsService mapsService,
                                      DriverRepository driverRepository,
                                      VehicleRepository vehicleRepository) {
        this.logisticsAssignmentRepository = logisticsAssignmentRepository;
        this.driverMatchingService = driverMatchingService;
        this.transportPricingService = transportPricingService;
        this.mapsService = mapsService;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public LogisticsAssignmentResponse createAssignment(CreateLogisticsRequest request) {
        BigDecimal distance = request.distanceKm() != null
                ? request.distanceKm()
                : mapsService.distanceKm(request.pickupAddress(), request.dropoffAddress());

        BigDecimal price = request.quotedPrice() != null
                ? request.quotedPrice()
                : transportPricingService.quote(distance, "STANDARD", null);

        LogisticsAssignment assignment = new LogisticsAssignment(
                request.orderId(),
                request.pickupAddress(),
                request.dropoffAddress(),
                price,
                "ETB",
                distance,
                "PENDING"
        );

        LogisticsAssignment saved = logisticsAssignmentRepository.save(assignment);
        return LogisticsAssignmentResponse.fromEntity(saved);
    }

    public LogisticsAssignmentResponse assignDriver(Long assignmentId, AssignDriverRequest request) {
        LogisticsAssignment assignment = logisticsAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics assignment not found: " + assignmentId));

        if (!driverRepository.existsById(request.driverId())) {
            throw new ResourceNotFoundException("Driver not found: " + request.driverId());
        }

        Long vehicleId = request.vehicleId();
        if (vehicleId == null) {
            List<Vehicle> vehicles = vehicleRepository.findByDriverId(request.driverId());
            if (!vehicles.isEmpty()) {
                vehicleId = vehicles.get(0).getId();
            }
        }

        assignment.assignDriver(request.driverId(), vehicleId);
        return LogisticsAssignmentResponse.fromEntity(logisticsAssignmentRepository.save(assignment));
    }

    public Long assign(Long orderId) {
        Long bestDriverId = driverMatchingService.findBestDriver(orderId);
        LogisticsAssignment assignment = logisticsAssignmentRepository.findByOrderId(orderId)
                .orElseGet(() -> {
                    BigDecimal distance = mapsService.distanceKm("Pickup", "Dropoff");
                    BigDecimal price = transportPricingService.quote(distance, "STANDARD", null);
                    return new LogisticsAssignment(orderId, "Pickup Point", "Delivery Point", price, "ETB", distance, "PENDING");
                });

        if (bestDriverId != null) {
            List<Vehicle> vehicles = vehicleRepository.findByDriverId(bestDriverId);
            Long vehicleId = vehicles.isEmpty() ? null : vehicles.get(0).getId();
            assignment.assignDriver(bestDriverId, vehicleId);
        }

        LogisticsAssignment saved = logisticsAssignmentRepository.save(assignment);
        return saved.getId();
    }

    public void updateStatus(Long assignmentId, String status) {
        LogisticsAssignment assignment = logisticsAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics assignment not found: " + assignmentId));
        assignment.updateStatus(status);
        logisticsAssignmentRepository.save(assignment);
    }

    @Transactional(readOnly = true)
    public LogisticsAssignmentResponse getAssignmentById(Long id) {
        LogisticsAssignment assignment = logisticsAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics assignment not found: " + id));
        return LogisticsAssignmentResponse.fromEntity(assignment);
    }

    @Transactional(readOnly = true)
    public LogisticsAssignmentResponse getAssignmentByOrderId(Long orderId) {
        LogisticsAssignment assignment = logisticsAssignmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Logistics assignment not found for order: " + orderId));
        return LogisticsAssignmentResponse.fromEntity(assignment);
    }

    @Transactional(readOnly = true)
    public Page<LogisticsAssignmentResponse> listAssignments(String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return logisticsAssignmentRepository.findByStatus(status, pageable)
                    .map(LogisticsAssignmentResponse::fromEntity);
        }
        return logisticsAssignmentRepository.findAll(pageable)
                .map(LogisticsAssignmentResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<LogisticsAssignmentResponse> listAssignmentsByDriver(Long driverId) {
        return logisticsAssignmentRepository.findByDriverId(driverId).stream()
                .map(LogisticsAssignmentResponse::fromEntity)
                .toList();
    }
}
