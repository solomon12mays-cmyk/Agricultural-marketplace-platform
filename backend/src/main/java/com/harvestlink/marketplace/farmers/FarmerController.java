package com.harvestlink.marketplace.farmers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
public class FarmerController {

    private final FarmerRepository farmerRepository;

    public FarmerController(FarmerRepository farmerRepository) {
        this.farmerRepository = farmerRepository;
    }

    @GetMapping("/farmers")
    public List<FarmerSummaryResponse> getFarmers() {
        return farmerRepository.findAllByOrderByNameAsc().stream()
                .map(FarmerSummaryResponse::from)
                .toList();
    }

    @GetMapping("/farmers/{slug}")
    public FarmerDetailResponse getFarmer(@PathVariable String slug) {
        return FarmerDetailResponse.from(findFarmerBySlug(slug));
    }

    @PatchMapping("/farmers/{slug}/profile")
    public FarmerSummaryResponse updateFarmerProfile(@PathVariable String slug,
                                                    @RequestBody FarmerProfileUpdateRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Farmer profile update payload is required");
        }

        Farmer farmer = findFarmerBySlug(slug);

        if (StringUtils.hasText(request.name())) {
            farmer.setName(request.name());
        }
        if (StringUtils.hasText(request.region())) {
            farmer.setRegion(request.region());
        }
        if (StringUtils.hasText(request.cropFocus())) {
            farmer.setCropFocus(request.cropFocus());
        }
        if (StringUtils.hasText(request.bio())) {
            farmer.setBio(request.bio());
        }
        if (request.rating() != null) {
            if (request.rating() < 0 || request.rating() > 5) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Farmer rating must be between 0 and 5");
            }
            farmer.setRating(request.rating());
        }
        if (request.harvestsThisSeason() != null) {
            farmer.setHarvestsThisSeason(request.harvestsThisSeason());
        }

        return FarmerSummaryResponse.from(farmerRepository.save(farmer));
    }

    private Farmer findFarmerBySlug(String slug) {
        return farmerRepository.findBySlugIgnoreCase(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found"));
    }

    public record FarmerSummaryResponse(Long id, String name, String slug, String region,
                                        String cropFocus, String farmSize, Double rating,
                                        Integer harvestsThisSeason) {
        public static FarmerSummaryResponse from(Farmer farmer) {
            return new FarmerSummaryResponse(
                    farmer.getId(),
                    farmer.getName(),
                    farmer.getSlug(),
                    farmer.getRegion(),
                    farmer.getCropFocus(),
                    farmer.getFarmSize(),
                    farmer.getRating(),
                    farmer.getHarvestsThisSeason());
        }
    }

    public record FarmerDetailResponse(Long id, String name, String slug, String region,
                                       String cropFocus, String farmSize, Double rating,
                                       String bio, String contactName, String contactPhone,
                                       Integer harvestsThisSeason) {
        public static FarmerDetailResponse from(Farmer farmer) {
            return new FarmerDetailResponse(
                    farmer.getId(),
                    farmer.getName(),
                    farmer.getSlug(),
                    farmer.getRegion(),
                    farmer.getCropFocus(),
                    farmer.getFarmSize(),
                    farmer.getRating(),
                    farmer.getBio(),
                    farmer.getContactName(),
                    farmer.getContactPhone(),
                    farmer.getHarvestsThisSeason());
        }
    }

    public record FarmerProfileUpdateRequest(String name, String region, String cropFocus,
                                             String bio, Double rating, Integer harvestsThisSeason) {
    }
}
