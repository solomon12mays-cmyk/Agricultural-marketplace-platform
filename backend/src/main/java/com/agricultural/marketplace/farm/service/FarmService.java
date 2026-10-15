package com.agricultural.marketplace.farm.service;

import java.util.List;

import com.agricultural.marketplace.farm.dto.CreateFarmLocationRequest;
import com.agricultural.marketplace.farm.dto.CreateFarmRequest;
import com.agricultural.marketplace.farm.dto.FarmLocationResponse;
import com.agricultural.marketplace.farm.dto.FarmResponse;
import com.agricultural.marketplace.farm.entity.Farm;
import com.agricultural.marketplace.farm.entity.FarmLocation;
import com.agricultural.marketplace.farm.repository.FarmLocationRepository;
import com.agricultural.marketplace.farm.repository.FarmRepository;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FarmService {

    private final FarmRepository farmRepository;
    private final FarmLocationRepository farmLocationRepository;

    public FarmService(FarmRepository farmRepository,
                       FarmLocationRepository farmLocationRepository) {
        this.farmRepository = farmRepository;
        this.farmLocationRepository = farmLocationRepository;
    }

    public List<FarmResponse> getFarmsByFarmerId(Long farmerId) {
        return farmRepository.findByFarmerId(farmerId).stream()
                .map(farm -> {
                    FarmLocation location = farmLocationRepository.findByFarmId(farm.getId()).orElse(null);
                    return FarmResponse.of(farm, location);
                })
                .toList();
    }

    public FarmResponse getFarmById(Long farmId) {
        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + farmId));
        FarmLocation location = farmLocationRepository.findByFarmId(farmId).orElse(null);
        return FarmResponse.of(farm, location);
    }

    @Transactional
    public FarmResponse createFarm(CreateFarmRequest request) {
        Farm farm = new Farm(
                request.farmerId(),
                request.name(),
                request.farmType(),
                request.sizeHectares(),
                request.certificationStatus()
        );
        Farm savedFarm = farmRepository.save(farm);

        FarmLocation savedLocation = null;
        if (request.location() != null) {
            FarmLocation location = new FarmLocation(
                    savedFarm.getId(),
                    request.location().region(),
                    request.location().zone(),
                    request.location().woreda(),
                    request.location().village(),
                    request.location().latitude(),
                    request.location().longitude()
            );
            savedLocation = farmLocationRepository.save(location);
        }

        return FarmResponse.of(savedFarm, savedLocation);
    }

    @Transactional
    public FarmLocationResponse setFarmLocation(Long farmId, CreateFarmLocationRequest request) {
        if (!farmRepository.existsById(farmId)) {
            throw new ResourceNotFoundException("Farm not found with id: " + farmId);
        }

        FarmLocation location = farmLocationRepository.findByFarmId(farmId)
                .map(existing -> new FarmLocation(
                        farmId,
                        request.region(),
                        request.zone(),
                        request.woreda(),
                        request.village(),
                        request.latitude(),
                        request.longitude()
                ))
                .orElseGet(() -> new FarmLocation(
                        farmId,
                        request.region(),
                        request.zone(),
                        request.woreda(),
                        request.village(),
                        request.latitude(),
                        request.longitude()
                ));

        return FarmLocationResponse.from(farmLocationRepository.save(location));
    }
}

