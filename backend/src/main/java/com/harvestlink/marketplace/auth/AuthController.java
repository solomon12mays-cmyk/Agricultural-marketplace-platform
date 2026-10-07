package com.harvestlink.marketplace.auth;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(AccountService accountService, AuthenticationManager authenticationManager,
                          SecurityContextRepository securityContextRepository) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @GetMapping("/csrf")
    public Map<String, String> csrfToken(CsrfToken csrfToken) {
        return Map.of("token", csrfToken.getToken());
    }

    @GetMapping("/roles")
    public java.util.List<AccountService.RoleSpec> registrationRoles() {
        return accountService.registrationRoles();
    }

    @PostMapping("/register")
    public AccountResponse register(@RequestBody AccountService.RegistrationRequest request) {
        return AccountResponse.from(accountService.register(request));
    }

    @PostMapping("/login")
    public AccountResponse login(@RequestBody LoginRequest request, HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse) {
        if (request == null || !StringUtils.hasText(request.loginId()) || !StringUtils.hasText(request.password())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number / operator username and password are required");
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            request.loginId().trim(), request.password()));
        } catch (AuthenticationException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid phone number or password");
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        if (servletRequest.getSession(false) != null) {
            servletRequest.changeSessionId();
        }
        securityContextRepository.saveContext(context, servletRequest, servletResponse);
        return AccountResponse.from((UserAccount) authentication.getPrincipal());
    }

    @GetMapping("/me")
    public AccountResponse currentAccount(Authentication authentication) {
        return AccountResponse.from(requireAccount(authentication));
    }

    @PostMapping("/change-password")
    public AccountResponse changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {
        if (request == null || !StringUtils.hasText(request.newPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new password is required");
        }
        return AccountResponse.from(accountService.changePassword(
                requireAccount(authentication), request.newPassword()));
    }

    @PostMapping("/logout")
    public void logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, authentication);
    }

    private UserAccount requireAccount(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserAccount account)) {
            throw new AuthenticationServiceException("Authenticated account could not be resolved");
        }
        return account;
    }

    public record LoginRequest(String loginId, String password) {
    }

    public record ChangePasswordRequest(String newPassword) {
    }

    public record AccountResponse(String loginId, String displayName, String phoneNumber,
                                  String role, boolean mustChangePassword) {
        static AccountResponse from(UserAccount account) {
            return new AccountResponse(
                    account.getLoginId(),
                    account.getDisplayName(),
                    account.getPhoneNumber(),
                    account.getRole().name(),
                    account.isMustChangePassword());
        }
    }
}
