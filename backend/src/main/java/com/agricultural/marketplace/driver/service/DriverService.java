package com.agricultural.marketplace.driver.service;

import java.util.Optional;

import com.agricultural.marketplace.driver.dto.CreateDriverRequest;
import com.agricultural.marketplace.driver.dto.DriverResponse;
import com.agricultural.marketplace.driver.entity.Driver;
import com.agricultural.marketplace.driver.repository.DriverRepository;
import com.agricultural.marketplace.shared.exception.ConflictException;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public DriverResponse registerDriver(CreateDriverRequest request) {
        if (driverRepository.findByPhone(request.phone()).isPresent()) {
            throw new ConflictException("Driver already registered with phone: " + request.phone());
        }
        if (driverRepository.findByLicenseNumber(request.licenseNumber()).isPresent()) {
            throw new ConflictException("Driver already registered with license: " + request.licenseNumber());
        }

        Driver driver = new Driver(
                request.userId(),
                request.fullName(),
                request.licenseNumber(),
                request.phone(),
                "PENDING_VERIFICATION"
        );
        Driver saved = driverRepository.save(driver);
        return DriverResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public DriverResponse getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));
        return DriverResponse.fromEntity(driver);
    }

    @Transactional(readOnly = true)
    public Optional<DriverResponse> getDriverByUserId(Long userId) {
        return driverRepository.findByUserId(userId)
                .map(DriverResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<DriverResponse> listDrivers(String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return driverRepository.findAll(pageable)
                    .map(DriverResponse::fromEntity);
        }
        return driverRepository.findAll(pageable).map(DriverResponse::fromEntity);
    }

    public DriverResponse updateDriverStatus(Long id, String status) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));
        driver.setStatus(status);
        return DriverResponse.fromEntity(driverRepository.save(driver));
    }
}
