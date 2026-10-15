package com.agricultural.marketplace.personalbuyer.service;

import java.util.Optional;

import com.agricultural.marketplace.personalbuyer.dto.CreatePersonalBuyerRequest;
import com.agricultural.marketplace.personalbuyer.dto.PersonalBuyerResponse;
import com.agricultural.marketplace.personalbuyer.dto.UpdateKycStatusRequest;
import com.agricultural.marketplace.personalbuyer.dto.UpdatePersonalBuyerRequest;
import com.agricultural.marketplace.personalbuyer.entity.PersonalBuyer;
import com.agricultural.marketplace.personalbuyer.repository.PersonalBuyerRepository;
import com.agricultural.marketplace.shared.exception.ConflictException;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersonalBuyerService {

    private final PersonalBuyerRepository personalBuyerRepository;

    public PersonalBuyerService(PersonalBuyerRepository personalBuyerRepository) {
        this.personalBuyerRepository = personalBuyerRepository;
    }

    public PersonalBuyerResponse createPersonalBuyer(CreatePersonalBuyerRequest request) {
        if (personalBuyerRepository.findByPhone(request.phone()).isPresent()) {
            throw new ConflictException("Personal buyer already registered with phone: " + request.phone());
        }
        if (request.email() != null && personalBuyerRepository.findByEmailIgnoreCase(request.email()).isPresent()) {
            throw new ConflictException("Personal buyer already registered with email: " + request.email());
        }

        PersonalBuyer pb = new PersonalBuyer(
                request.userId(),
                request.fullName(),
                request.phone(),
                request.email(),
                request.region(),
                "PENDING"
        );
        PersonalBuyer saved = personalBuyerRepository.save(pb);
        return PersonalBuyerResponse.fromEntity(saved);
    }

    public PersonalBuyerResponse updatePersonalBuyer(Long id, UpdatePersonalBuyerRequest request) {
        PersonalBuyer pb = personalBuyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personal buyer not found with id: " + id));

        pb.updateProfile(request.fullName(), request.email(), request.region());
        return PersonalBuyerResponse.fromEntity(personalBuyerRepository.save(pb));
    }

    public PersonalBuyerResponse updateKycStatus(Long id, UpdateKycStatusRequest request) {
        PersonalBuyer pb = personalBuyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personal buyer not found with id: " + id));

        pb.updateKycStatus(request.kycStatus().toUpperCase());
        return PersonalBuyerResponse.fromEntity(personalBuyerRepository.save(pb));
    }

    @Transactional(readOnly = true)
    public PersonalBuyerResponse getById(Long id) {
        PersonalBuyer pb = personalBuyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personal buyer not found with id: " + id));
        return PersonalBuyerResponse.fromEntity(pb);
    }

    @Transactional(readOnly = true)
    public Optional<PersonalBuyerResponse> getByUserId(Long userId) {
        return personalBuyerRepository.findByUserId(userId)
                .map(PersonalBuyerResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<PersonalBuyerResponse> listPersonalBuyers(String kycStatus, Pageable pageable) {
        if (kycStatus != null && !kycStatus.isBlank()) {
            return personalBuyerRepository.findByKycStatus(kycStatus.toUpperCase(), pageable)
                    .map(PersonalBuyerResponse::fromEntity);
        }
        return personalBuyerRepository.findAll(pageable)
                .map(PersonalBuyerResponse::fromEntity);
    }
}
