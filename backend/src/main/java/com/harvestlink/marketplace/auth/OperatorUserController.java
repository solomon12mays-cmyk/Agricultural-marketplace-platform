package com.harvestlink.marketplace.auth;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/operator/users")
public class OperatorUserController {

    private final AccountService accountService;

    public OperatorUserController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public AccountService.PageResult<UserResponse> listUsers(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "role", required = false) String role,
            @RequestParam(name = "active", required = false) Boolean active,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        AccountService.PageResult<UserAccount> result = accountService.listUsers(search, role, active, page, size);
        return new AccountService.PageResult<>(
                result.items().stream().map(UserResponse::from).toList(),
                result.page(),
                result.size(),
                result.totalItems(),
                result.totalPages());
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable long id) {
        return UserResponse.from(accountService.getUser(id));
    }

    @PatchMapping("/{id}")
    public UserResponse updateUser(@PathVariable long id, @RequestBody AccountService.UserUpdateRequest request) {
        return UserResponse.from(accountService.updateUser(id, request));
    }

    @PatchMapping("/{id}/status")
    public UserResponse setStatus(@PathVariable long id,
                                  @RequestBody StatusRequest request,
                                  Authentication authentication) {
        if (request == null || request.active() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "An active status is required");
        }
        return UserResponse.from(accountService.setUserActive(id, request.active(),
                requireAccount(authentication).getId()));
    }

    @PostMapping("/{id}/reset-password")
    public TemporaryPasswordResponse resetPassword(@PathVariable long id) {
        AccountService.TemporaryAccount result = accountService.resetPassword(id);
        return new TemporaryPasswordResponse(
                result.account().getId(),
                result.account().getLoginId(),
                result.temporaryPassword(),
                true);
    }

    private UserAccount requireAccount(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserAccount account)) {
            throw new org.springframework.security.authentication.AuthenticationServiceException(
                    "Authenticated account could not be resolved");
        }
        return account;
    }

    public record StatusRequest(Boolean active) {
    }

    public record TemporaryPasswordResponse(long id, String loginId, String temporaryPassword,
                                            boolean mustChangePassword) {
    }

    public record UserResponse(long id, String loginId, String displayName, String phoneNumber,
                               String role, boolean active, boolean mustChangePassword,
                               String fanNumber, String organizationName, String contactPersonName,
                               String tinNumber, String licenseNumber, Long farmerId) {
        static UserResponse from(UserAccount account) {
            return new UserResponse(
                    account.getId(),
                    account.getLoginId(),
                    account.getDisplayName(),
                    account.getPhoneNumber(),
                    account.getRole().name(),
                    account.isActive(),
                    account.isMustChangePassword(),
                    account.getFanNumber(),
                    account.getOrganizationName(),
                    account.getContactPersonName(),
                    account.getTinNumber(),
                    account.getLicenseNumber(),
                    account.getFarmerId());
        }
    }
}
