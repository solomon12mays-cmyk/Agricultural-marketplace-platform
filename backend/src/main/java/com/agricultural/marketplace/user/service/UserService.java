package com.agricultural.marketplace.user.service;

import java.util.List;

import com.agricultural.marketplace.auth.entity.Role;
import com.agricultural.marketplace.auth.entity.User;
import com.agricultural.marketplace.auth.entity.UserRole;
import com.agricultural.marketplace.auth.entity.UserSession;
import com.agricultural.marketplace.auth.repository.RoleRepository;
import com.agricultural.marketplace.auth.repository.UserRepository;
import com.agricultural.marketplace.auth.repository.UserRoleRepository;
import com.agricultural.marketplace.auth.repository.UserSessionRepository;
import com.agricultural.marketplace.shared.exception.ConflictException;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import com.agricultural.marketplace.user.dto.UpdateUserRequest;
import com.agricultural.marketplace.user.dto.UserResponse;
import com.agricultural.marketplace.user.dto.UserSessionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final UserSessionRepository userSessionRepository;

    public UserService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       RoleRepository roleRepository,
                       UserSessionRepository userSessionRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.userSessionRepository = userSessionRepository;
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        List<String> roles = userRoleRepository.findRoleCodesByUserId(user.getId());
        return UserResponse.of(user, roles);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        List<String> roles = userRoleRepository.findRoleCodesByUserId(user.getId());
        return UserResponse.of(user, roles);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> listUsers(String status, Pageable pageable) {
        return userRepository.findAll(pageable).map(user -> {
            List<String> roles = userRoleRepository.findRoleCodesByUserId(user.getId());
            return UserResponse.of(user, roles);
        });
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (request.email() != null && !request.email().equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmailIgnoreCase(request.email())) {
                throw new ConflictException("Email is already taken: " + request.email());
            }
            user.setEmail(request.email());
        }

        if (request.phone() != null && !request.phone().equals(user.getPhone())) {
            if (userRepository.existsByPhone(request.phone())) {
                throw new ConflictException("Phone number is already taken: " + request.phone());
            }
            user.setPhone(request.phone());
        }

        User saved = userRepository.save(user);
        List<String> roles = userRoleRepository.findRoleCodesByUserId(saved.getId());
        return UserResponse.of(saved, roles);
    }

    public UserResponse updateStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setStatus(status.toUpperCase());
        User saved = userRepository.save(user);
        List<String> roles = userRoleRepository.findRoleCodesByUserId(saved.getId());
        return UserResponse.of(saved, roles);
    }

    public UserResponse unlockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.unlock();
        User saved = userRepository.save(user);
        List<String> roles = userRoleRepository.findRoleCodesByUserId(saved.getId());
        return UserResponse.of(saved, roles);
    }

    public List<String> assignRole(Long userId, String roleCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Role role = roleRepository.findByCodeIgnoreCase(roleCode)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with code: " + roleCode));

        if (userRoleRepository.findByUserIdAndRoleId(user.getId(), role.getId()).isEmpty()) {
            userRoleRepository.save(new UserRole(user.getId(), role.getId()));
        }

        return userRoleRepository.findRoleCodesByUserId(userId);
    }

    public List<String> revokeRole(Long userId, String roleCode) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        Role role = roleRepository.findByCodeIgnoreCase(roleCode)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with code: " + roleCode));

        userRoleRepository.findByUserIdAndRoleId(userId, role.getId())
                .ifPresent(userRoleRepository::delete);

        return userRoleRepository.findRoleCodesByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<String> getUserRoles(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return userRoleRepository.findRoleCodesByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<UserSessionResponse> getUserSessions(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return userSessionRepository.findByUserId(userId).stream()
                .map(UserSessionResponse::fromEntity)
                .toList();
    }

    public void revokeAllSessions(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        userSessionRepository.deleteByUserIdAndRevokedAtIsNull(userId);
    }
}

