package com.agricultural.marketplace.farmer.service;

import java.util.List;

import com.agricultural.marketplace.farmer.dto.CreateFarmerProfileRequest;
import com.agricultural.marketplace.farmer.dto.FarmerResponse;
import com.agricultural.marketplace.farmer.dto.UpdateFarmerProfileRequest;
import com.agricultural.marketplace.farmer.entity.Farmer;
import com.agricultural.marketplace.farmer.repository.FarmerRepository;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FarmerService {

    private final FarmerRepository farmerRepository;
    private final FarmerRegistrationService registrationService;
    private final FarmerVerificationService verificationService;
    private final FarmerAccountService accountService;

    public FarmerService(FarmerRepository farmerRepository,
                         FarmerRegistrationService registrationService,
                         FarmerVerificationService verificationService,
                         FarmerAccountService accountService) {
        this.farmerRepository = farmerRepository;
        this.registrationService = registrationService;
        this.verificationService = verificationService;
        this.accountService = accountService;
    }

    public List<FarmerResponse> getAllFarmers() {
        return farmerRepository.findAll().stream()
                .map(FarmerResponse::from)
                .toList();
    }

    public FarmerResponse getProfileById(Long id) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id: " + id));
        return FarmerResponse.from(farmer);
    }

    public FarmerResponse getProfileByUserId(Long userId) {
        Farmer farmer = farmerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found for user id: " + userId));
        return FarmerResponse.from(farmer);
    }

    @Transactional
    public FarmerResponse registerFarmer(Long userId, CreateFarmerProfileRequest request) {
        return registrationService.register(userId, request);
    }

    @Transactional
    public FarmerResponse updateProfile(Long farmerId, UpdateFarmerProfileRequest request) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id: " + farmerId));
        farmer.updateProfile(request.fullName(), request.region(), request.zone(), request.woreda());
        return FarmerResponse.from(farmerRepository.save(farmer));
    }

    @Transactional
    public FarmerResponse verifyFarmer(Long farmerId) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id: " + farmerId));
        farmer.setVerificationStatus("VERIFIED");
        return FarmerResponse.from(farmerRepository.save(farmer));
    }
}
