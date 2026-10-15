package com.agricultural.marketplace.personalbuyer.repository;

import java.util.Optional;

import com.agricultural.marketplace.personalbuyer.entity.PersonalBuyer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalBuyerRepository extends JpaRepository<PersonalBuyer, Long> {

    Optional<PersonalBuyer> findByUserId(Long userId);

    Optional<PersonalBuyer> findByPhone(String phone);

    Optional<PersonalBuyer> findByEmailIgnoreCase(String email);

    Page<PersonalBuyer> findByKycStatus(String kycStatus, Pageable pageable);
}

