package com.agricultural.marketplace.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agricultural.marketplace.auth.entity.RolePermission;

/**
 * Persistence for role-to-permission grants.
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Long permissionId);

    void deleteByRoleId(Long roleId);
}
