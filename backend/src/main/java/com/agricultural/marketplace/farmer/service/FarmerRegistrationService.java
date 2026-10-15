package com.agricultural.marketplace.farmer.service;

import com.agricultural.marketplace.farmer.dto.CreateFarmerProfileRequest;
import com.agricultural.marketplace.farmer.dto.FarmerResponse;
import com.agricultural.marketplace.farmer.entity.Farmer;
import com.agricultural.marketplace.farmer.repository.FarmerRepository;
import com.agricultural.marketplace.shared.exception.ConflictException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FarmerRegistrationService {

    private final FarmerRepository farmerRepository;

    public FarmerRegistrationService(FarmerRepository farmerRepository) {
        this.farmerRepository = farmerRepository;
    }

    @Transactional
    public FarmerResponse register(Long userId, CreateFarmerProfileRequest request) {
        if (farmerRepository.findByNationalId(request.nationalId()).isPresent()) {
            throw new ConflictException("Farmer with national ID already exists: " + request.nationalId());
        }
        if (farmerRepository.findByPhone(request.phone()).isPresent()) {
            throw new ConflictException("Farmer with phone number already exists: " + request.phone());
        }

        Farmer farmer = new Farmer(
                userId,
                request.fullName(),
                request.nationalId(),
                request.phone(),
                request.region(),
                request.zone(),
                request.woreda(),
                "PENDING"
        );
        Farmer saved = farmerRepository.save(farmer);
        return FarmerResponse.from(saved);
    }
}
