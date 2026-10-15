package com.agricultural.marketplace.logistics.service;

import java.util.List;

import com.agricultural.marketplace.driver.entity.Driver;
import com.agricultural.marketplace.driver.entity.Vehicle;
import com.agricultural.marketplace.driver.repository.DriverRepository;
import com.agricultural.marketplace.driver.repository.VehicleRepository;
import org.springframework.stereotype.Service;

/**
 * Finds the best available driver/vehicle for a transport job.
 */
@Service
public class DriverMatchingService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public DriverMatchingService(DriverRepository driverRepository, VehicleRepository vehicleRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Long findBestDriver(Long orderId) {
        List<Driver> activeDrivers = driverRepository.findByStatus("ACTIVE");
        if (activeDrivers.isEmpty()) {
            activeDrivers = driverRepository.findAll();
        }

        for (Driver driver : activeDrivers) {
            List<Vehicle> vehicles = vehicleRepository.findByDriverId(driver.getId());
            if (!vehicles.isEmpty()) {
                return driver.getId();
            }
        }

        return activeDrivers.isEmpty() ? null : activeDrivers.get(0).getId();
    }
}
