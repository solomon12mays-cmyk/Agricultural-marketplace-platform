package com.agricultural.marketplace.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.agricultural.marketplace.auth.entity.Permission;

/**
 * Persistence for individual permission grants.
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByCodeIgnoreCase(String code);

    List<Permission> findByCodeIn(List<String> codes);

    @Query("""
            select permission.code
              from RolePermission rolePermission
              join Permission permission on permission.id = rolePermission.permissionId
             where rolePermission.roleId = :roleId
            """)
    List<String> findPermissionCodesByRoleId(@Param("roleId") Long roleId);
}
