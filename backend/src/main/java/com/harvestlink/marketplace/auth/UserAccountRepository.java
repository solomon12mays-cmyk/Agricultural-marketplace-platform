package com.harvestlink.marketplace.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long>, JpaSpecificationExecutor<UserAccount> {
    Optional<UserAccount> findByLoginIdIgnoreCase(String loginId);
    boolean existsByLoginIdIgnoreCase(String loginId);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);
    boolean existsByFanNumberIgnoreCase(String fanNumber);
    boolean existsByTinNumberIgnoreCase(String tinNumber);
    boolean existsByLicenseNumberIgnoreCase(String licenseNumber);
    long countByRoleAndActiveTrue(AccountRole role);
}
