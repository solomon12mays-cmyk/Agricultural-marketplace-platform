package com.agricultural.marketplace.driver.service;

import java.util.List;

import com.agricultural.marketplace.driver.dto.CreateVehicleRequest;
import com.agricultural.marketplace.driver.dto.VehicleResponse;
import com.agricultural.marketplace.driver.entity.Vehicle;
import com.agricultural.marketplace.driver.repository.DriverRepository;
import com.agricultural.marketplace.driver.repository.VehicleRepository;
import com.agricultural.marketplace.shared.exception.ConflictException;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    public VehicleService(VehicleRepository vehicleRepository, DriverRepository driverRepository) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }

    public VehicleResponse addVehicle(Long driverId, CreateVehicleRequest request) {
        if (!driverRepository.existsById(driverId)) {
            throw new ResourceNotFoundException("Driver not found with id: " + driverId);
        }
        if (vehicleRepository.findByPlateNumber(request.plateNumber()).isPresent()) {
            throw new ConflictException("Vehicle already registered with plate: " + request.plateNumber());
        }

        Vehicle vehicle = new Vehicle(
                driverId,
                request.plateNumber(),
                request.vehicleType(),
                request.capacityKg(),
                "ACTIVE"
        );
        Vehicle saved = vehicleRepository.save(vehicle);
        return VehicleResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> getVehiclesByDriver(Long driverId) {
        return vehicleRepository.findByDriverId(driverId).stream()
                .map(VehicleResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        return VehicleResponse.fromEntity(vehicle);
    }

    public VehicleResponse updateVehicleStatus(Long id, String status) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        vehicle.setStatus(status);
        return VehicleResponse.fromEntity(vehicleRepository.save(vehicle));
    }
}
