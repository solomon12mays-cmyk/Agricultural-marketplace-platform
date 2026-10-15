package com.agricultural.marketplace.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.agricultural.marketplace.auth.entity.UserRole;

/**
 * Persistence for role assignments, including joined role-code lookups.
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("""
            select role.code
              from UserRole userRole
              join Role role on role.id = userRole.roleId
             where userRole.userId = :userId
            """)
    List<String> findRoleCodesByUserId(@Param("userId") Long userId);

    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);

    void deleteByUserId(Long userId);
}
