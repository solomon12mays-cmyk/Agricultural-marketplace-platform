package com.agricultural.marketplace.shared.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.agricultural.marketplace.auth.entity.User;
import com.agricultural.marketplace.auth.repository.UserRepository;
import com.agricultural.marketplace.auth.repository.UserRoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Bridges the auth module's user accounts to Spring Security, reflecting
 * account state (enabled, locked) and assigned roles in the principal.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public CustomUserDetailsService(UserRepository userRepository,
                                    UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found: " + username));
        List<String> roleCodes = userRoleRepository.findRoleCodesByUserId(user.getId());
        return new AccountPrincipal(user, roleCodes);
    }

    private static boolean isLocked(User user) {
        return "LOCKED".equalsIgnoreCase(user.getStatus())
                || (user.getLockedUntil() != null && user.getLockedUntil().isAfter(java.time.LocalDateTime.now()));
    }

    /**
     * Spring Security principal for a platform account.
     */
    public static final class AccountPrincipal implements UserDetails {

        private final User user;
        private final List<String> roleCodes;

        private AccountPrincipal(User user, List<String> roleCodes) {
            this.user = user;
            this.roleCodes = List.copyOf(roleCodes);
        }

        public Long getId() {
            return user.getId();
        }

        public boolean isPasswordChangeRequired() {
            return user.isPasswordChangeRequired();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String roleCode : roleCodes) {
                authorities.add(new SimpleGrantedAuthority(
                        "ROLE_" + roleCode.toUpperCase(Locale.ROOT)));
            }
            if (authorities.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
            return authorities;
        }

        @Override
        public String getPassword() {
            return user.getPasswordHash();
        }

        @Override
        public String getUsername() {
            return user.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return !isLocked(user);
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return "ACTIVE".equalsIgnoreCase(user.getStatus()) && !isLocked(user);
        }
    }
}
