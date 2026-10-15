package com.agricultural.marketplace.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agricultural.marketplace.auth.entity.Role;

/**
 * Persistence for named roles.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByCodeIgnoreCase(String code);
}
