package com.agricultural.marketplace.user;

import java.util.List;
import java.util.Optional;

import com.agricultural.marketplace.auth.entity.Role;
import com.agricultural.marketplace.auth.entity.User;
import com.agricultural.marketplace.auth.repository.RoleRepository;
import com.agricultural.marketplace.auth.repository.UserRepository;
import com.agricultural.marketplace.auth.repository.UserRoleRepository;
import com.agricultural.marketplace.auth.repository.UserSessionRepository;
import com.agricultural.marketplace.user.dto.UserResponse;
import com.agricultural.marketplace.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserSessionRepository userSessionRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userRoleRepository, roleRepository, userSessionRepository);
    }

    @Test
    @DisplayName("getUserById should return user response with roles")
    void testGetUserById() {
        User user = new User("testuser", "test@example.com", "+251911223344", "hash", "ACTIVE");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRoleRepository.findRoleCodesByUserId(any())).thenReturn(List.of("FARMER"));

        UserResponse response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals("testuser", response.username());
        assertEquals("test@example.com", response.email());
        assertEquals(List.of("FARMER"), response.roles());
    }

    @Test
    @DisplayName("unlockUser should reset failed login attempts and reactivate account")
    void testUnlockUser() {
        User user = new User("lockeduser", "locked@example.com", "+251911223345", "hash", "LOCKED");
        user.setFailedLoginAttempts(5);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRoleRepository.findRoleCodesByUserId(any())).thenReturn(List.of("BUYER"));

        UserResponse response = userService.unlockUser(1L);

        assertEquals("ACTIVE", response.status());
        assertEquals(0, response.failedLoginAttempts());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("assignRole should save user role assignment")
    void testAssignRole() {
        User user = new User("testuser", "test@example.com", "+251911223344", "hash", "ACTIVE");
        Role role = new Role("DRIVER", "Driver", "Transport driver");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByCodeIgnoreCase("DRIVER")).thenReturn(Optional.of(role));
        when(userRoleRepository.findByUserIdAndRoleId(any(), any())).thenReturn(Optional.empty());
        when(userRoleRepository.findRoleCodesByUserId(1L)).thenReturn(List.of("DRIVER"));

        List<String> roles = userService.assignRole(1L, "DRIVER");

        assertEquals(List.of("DRIVER"), roles);
        verify(userRoleRepository).save(any());
    }
}

