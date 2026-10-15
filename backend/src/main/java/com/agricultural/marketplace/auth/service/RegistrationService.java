package com.agricultural.marketplace.auth.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.agricultural.marketplace.auth.entity.Role;
import com.agricultural.marketplace.auth.entity.User;
import com.agricultural.marketplace.auth.entity.UserRole;
import com.agricultural.marketplace.auth.repository.RoleRepository;
import com.agricultural.marketplace.auth.repository.UserRepository;
import com.agricultural.marketplace.auth.repository.UserRoleRepository;
import com.agricultural.marketplace.auth.dto.RegisterRequest;
import com.agricultural.marketplace.shared.audit.AuditAction;
import com.agricultural.marketplace.shared.audit.AuditService;
import com.agricultural.marketplace.shared.exception.ConflictException;
import com.agricultural.marketplace.shared.exception.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Creates new accounts with uniqueness enforcement, password policy checks
 * and a default role.
 */
@Service
public class RegistrationService {

    private static final String DEFAULT_ROLE_CODE = "BUYER";
    private static final List<String> ALLOWED_ROLE_CODES = List.of("BUYER", "FARMER", "DRIVER", "OPERATOR");

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordService passwordService;
    private final AuditService auditService;

    public RegistrationService(UserRepository userRepository,
                               RoleRepository roleRepository,
                               UserRoleRepository userRoleRepository,
                               PasswordService passwordService,
                               AuditService auditService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordService = passwordService;
        this.auditService = auditService;
    }

    @Transactional
    public Long register(RegisterRequest request) {
        if (!StringUtils.hasText(request.username()) || !StringUtils.hasText(request.username().trim())) {
            throw new ValidationException("Username is required", Map.of("username", "Required"));
        }
        if (!passwordService.isStrongEnough(request.password())) {
            throw new ValidationException("Password does not meet the policy",
                    Map.of("password", "Must be at least 10 characters"));
        }

        String username = request.username().trim();
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new ConflictException("An account already uses this username");
        }
        if (StringUtils.hasText(request.email()) && userRepository.existsByEmailIgnoreCase(request.email().trim())) {
            throw new ConflictException("An account already uses this email address");
        }
        if (StringUtils.hasText(request.phone()) && userRepository.existsByPhone(request.phone().trim())) {
            throw new ConflictException("An account already uses this phone number");
        }

        User user = new User(
                username,
                StringUtils.hasText(request.email()) ? request.email().trim() : null,
                StringUtils.hasText(request.phone()) ? request.phone().trim() : null,
                passwordService.encode(request.password()),
                "ACTIVE");
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        assignDefaultRole(savedUser);
        auditService.record("USER", Long.toString(savedUser.getId()), AuditAction.CREATE,
                username, "Account registered with role " + DEFAULT_ROLE_CODE);
        return savedUser.getId();
    }

    private void assignDefaultRole(User user) {
        Role role = roleRepository.findByCodeIgnoreCase(DEFAULT_ROLE_CODE)
                .orElseGet(() -> roleRepository.save(new Role(
                        DEFAULT_ROLE_CODE, "Buyer", "Platform buyer account")));
        userRoleRepository.save(new UserRole(user.getId(), role.getId()));
    }

    static boolean isAllowedRole(String roleCode) {
        return roleCode != null && ALLOWED_ROLE_CODES.contains(roleCode.toUpperCase(java.util.Locale.ROOT));
    }
}
