package com.agricultural.marketplace.organization.repository;

import java.util.Optional;

import com.agricultural.marketplace.organization.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByUserId(Long userId);

    Optional<Organization> findByTaxId(String taxId);

    Optional<Organization> findByRegistrationNumber(String registrationNumber);

    Page<Organization> findByVerificationStatus(String verificationStatus, Pageable pageable);
}

