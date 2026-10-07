package com.harvestlink.marketplace.auth;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.harvestlink.marketplace.farmers.Farmer;
import com.harvestlink.marketplace.farmers.FarmerRepository;

@Service
public class AccountService {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9][0-9]{7,14}$");
    private static final int MIN_PASSWORD_LENGTH = 10;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserAccountRepository accountRepository;
    private final FarmerRepository farmerRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(UserAccountRepository accountRepository, FarmerRepository farmerRepository,
                          PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.farmerRepository = farmerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserAccount register(RegistrationRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Registration details are required");
        }
        AccountRole role = resolveRegistrationRole(request.role());
        return switch (role) {
            case FARMER -> registerFarmer(request);
            case ORGANIZATION -> registerOrganization(request);
            case PERSONAL_BUYER -> registerPersonalBuyer(request);
            case DRIVER -> registerDriver(request);
            case OPERATOR -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Operator accounts are provisioned by an administrator");
        };
    }

    public List<RoleSpec> registrationRoles() {
        return List.of(
                new RoleSpec("FARMER", "Farmer", "Grower selling produce from their own farm",
                        List.of(
                                new RoleField("fullName", "Full name", "text"),
                                new RoleField("fanNumber", "FAN / national ID", "text"),
                                new RoleField("phoneNumber", "Phone number", "tel"),
                                new RoleField("password", "Password", "password"))),
                new RoleSpec("ORGANIZATION", "Organization", "Cooperative, union, or agribusiness seller",
                        List.of(
                                new RoleField("organizationName", "Organization name", "text"),
                                new RoleField("tinNumber", "TIN / registration number", "text"),
                                new RoleField("contactPersonName", "Contact person name", "text"),
                                new RoleField("phoneNumber", "Contact phone number", "tel"),
                                new RoleField("password", "Password", "password"))),
                new RoleSpec("PERSONAL_BUYER", "Personal buyer", "Individual purchasing produce",
                        List.of(
                                new RoleField("fullName", "Full name", "text"),
                                new RoleField("phoneNumber", "Phone number", "tel"),
                                new RoleField("password", "Password", "password"))),
                new RoleSpec("DRIVER", "Driver", "Logistics provider moving shipments",
                        List.of(
                                new RoleField("fullName", "Full name", "text"),
                                new RoleField("fanNumber", "FAN / national ID", "text"),
                                new RoleField("licenseNumber", "Driver license number", "text"),
                                new RoleField("phoneNumber", "Phone number", "tel"),
                                new RoleField("password", "Password", "password"))));
    }

    private AccountRole resolveRegistrationRole(String role) {
        if (!StringUtils.hasText(role)) {
            return AccountRole.FARMER;
        }
        String normalized = role.trim().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
        return switch (normalized) {
            case "FARMER" -> AccountRole.FARMER;
            case "ORGANIZATION" -> AccountRole.ORGANIZATION;
            case "PERSONAL_BUYER" -> AccountRole.PERSONAL_BUYER;
            case "DRIVER" -> AccountRole.DRIVER;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown registration role. Valid roles: farmer, organization, personal_buyer, driver");
        };
    }

    private UserAccount registerFarmer(RegistrationRequest request) {
        validateFarmerDetails(request.fullName(), request.fanNumber(), request.phoneNumber());
        validatePassword(request.password());
        String phone = normalizePhone(request.phoneNumber());
        String fanNumber = request.fanNumber().trim();
        ensureIdentityAvailable(phone, fanNumber, null, null);

        Farmer farmer = farmerRepository.save(new Farmer(
                request.fullName().trim(),
                createFarmerSlug(request.fullName()),
                "Not provided",
                "Not provided",
                "Not provided",
                4.8,
                null,
                request.fullName().trim(),
                phone,
                0));

        return accountRepository.save(new UserAccount(
                phone,
                passwordEncoder.encode(request.password()),
                request.fullName().trim(),
                phone,
                fanNumber,
                farmer.getId(),
                AccountRole.FARMER,
                false));
    }

    private UserAccount registerOrganization(RegistrationRequest request) {
        String organizationName = requireText(request.organizationName(), 120,
                "Organization name is required and must be at most 120 characters");
        String contactPersonName = requireText(request.contactPersonName(), 120,
                "Contact person name is required and must be at most 120 characters");
        String tinNumber = requireText(request.tinNumber(), 80,
                "TIN / registration number is required and must be at most 80 characters");
        validatePhoneNumber(request.phoneNumber());
        validatePassword(request.password());
        String phone = normalizePhone(request.phoneNumber());
        ensureIdentityAvailable(phone, null, tinNumber, null);

        return accountRepository.save(new UserAccount(
                phone,
                passwordEncoder.encode(request.password()),
                organizationName,
                phone,
                null,
                null,
                AccountRole.ORGANIZATION,
                false,
                organizationName,
                contactPersonName,
                tinNumber,
                null));
    }

    private UserAccount registerPersonalBuyer(RegistrationRequest request) {
        String fullName = requireText(request.fullName(), 120,
                "Full name is required and must be at most 120 characters");
        validatePhoneNumber(request.phoneNumber());
        validatePassword(request.password());
        String phone = normalizePhone(request.phoneNumber());
        ensureIdentityAvailable(phone, null, null, null);

        return accountRepository.save(new UserAccount(
                phone,
                passwordEncoder.encode(request.password()),
                fullName,
                phone,
                null,
                null,
                AccountRole.PERSONAL_BUYER,
                false));
    }

    private UserAccount registerDriver(RegistrationRequest request) {
        String fullName = requireText(request.fullName(), 120,
                "Full name is required and must be at most 120 characters");
        String fanNumber = requireText(request.fanNumber(), 80,
                "FAN / national ID is required and must be at most 80 characters");
        String licenseNumber = requireText(request.licenseNumber(), 80,
                "Driver license number is required and must be at most 80 characters");
        validatePhoneNumber(request.phoneNumber());
        validatePassword(request.password());
        String phone = normalizePhone(request.phoneNumber());
        ensureIdentityAvailable(phone, fanNumber, null, licenseNumber);

        return accountRepository.save(new UserAccount(
                phone,
                passwordEncoder.encode(request.password()),
                fullName,
                phone,
                fanNumber,
                null,
                AccountRole.DRIVER,
                false,
                null,
                null,
                null,
                licenseNumber));
    }

    @Transactional
    public TemporaryAccount provisionFarmer(FarmerProvisionRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Farmer details are required");
        }
        validateFarmerDetails(request.fullName(), request.fanNumber(), request.phoneNumber());
        String phone = normalizePhone(request.phoneNumber());
        String fanNumber = request.fanNumber().trim();
        ensureIdentityAvailable(phone, fanNumber, null, null);

        String temporaryPassword = createTemporaryPassword();
        Farmer farmer = farmerRepository.save(new Farmer(
                request.fullName().trim(),
                createFarmerSlug(request.fullName()),
                "Not provided",
                "Not provided",
                "Not provided",
                4.8,
                null,
                request.fullName().trim(),
                phone,
                0));

        UserAccount account = accountRepository.save(new UserAccount(
                phone,
                passwordEncoder.encode(temporaryPassword),
                request.fullName().trim(),
                phone,
                fanNumber,
                farmer.getId(),
                AccountRole.FARMER,
                true));
        return new TemporaryAccount(account, temporaryPassword);
    }

    public PageResult<UserAccount> listUsers(String search, String role, Boolean active, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);

        Specification<UserAccount> specification = (root, query, criteriaBuilder) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase(Locale.ROOT) + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("loginId")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("displayName")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("fanNumber")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("tinNumber")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("licenseNumber")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("organizationName")), pattern)));
            }
            if (StringUtils.hasText(role)) {
                predicates.add(root.get("role").in(parseRoleFilter(role)));
            }
            if (active != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }
            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        Page<UserAccount> result = accountRepository.findAll(specification,
                PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "id")));
        return new PageResult<>(result.getContent(), result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
    }

    public UserAccount getUser(long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional
    public UserAccount updateUser(long id, UserUpdateRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Update details are required");
        }
        UserAccount account = getUser(id);

        if (request.displayName() != null) {
            String displayName = requireText(request.displayName(), 120,
                    "Display name is required and must be at most 120 characters");
            account.setDisplayName(displayName);
        }
        if (request.phoneNumber() != null) {
            validatePhoneNumber(request.phoneNumber());
            String phone = normalizePhone(request.phoneNumber());
            if (!phone.equals(account.getPhoneNumber()) && accountRepository.existsByPhoneNumberAndIdNot(phone, id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "An account already uses this phone number");
            }
            boolean phoneLogin = account.getLoginId() != null
                    && account.getLoginId().equals(account.getPhoneNumber());
            account.setPhoneNumber(phone);
            // Phone-login accounts sign in with their phone number, so keep loginId in step.
            if (phoneLogin && !phone.equals(account.getLoginId())
                    && !accountRepository.existsByLoginIdIgnoreCase(phone)) {
                account.setLoginId(phone);
            }
        }
        if (request.role() != null) {
            AccountRole newRole = resolveRegistrationRole(request.role());
            if (account.getRole() == AccountRole.OPERATOR && newRole != AccountRole.OPERATOR
                    && !hasAnotherActiveOperator(account)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Promote another operator before demoting the last active operator");
            }
            account.setRole(newRole);
        }
        if (request.organizationName() != null) {
            account.setOrganizationName(requireText(request.organizationName(), 120,
                    "Organization name is required and must be at most 120 characters"));
        }
        if (request.contactPersonName() != null) {
            account.setContactPersonName(requireText(request.contactPersonName(), 120,
                    "Contact person name is required and must be at most 120 characters"));
        }
        return accountRepository.save(account);
    }

    @Transactional
    public UserAccount setUserActive(long id, boolean active, long actingAccountId) {
        UserAccount account = getUser(id);
        if (!active) {
            if (account.getId() == actingAccountId) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "You cannot deactivate your own account");
            }
            if (account.getRole() == AccountRole.OPERATOR && !hasAnotherActiveOperator(account)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Cannot deactivate the last active operator");
            }
        }
        account.setActive(active);
        return accountRepository.save(account);
    }

    @Transactional
    public TemporaryAccount resetPassword(long id) {
        UserAccount account = getUser(id);
        String temporaryPassword = createTemporaryPassword();
        account.setPasswordHash(passwordEncoder.encode(temporaryPassword));
        account.setMustChangePassword(true);
        accountRepository.save(account);
        return new TemporaryAccount(account, temporaryPassword);
    }

    private boolean hasAnotherActiveOperator(UserAccount account) {
        long activeOperators = accountRepository.countByRoleAndActiveTrue(AccountRole.OPERATOR);
        long selfAsActiveOperator = account.getRole() == AccountRole.OPERATOR && account.isActive() ? 1 : 0;
        return activeOperators - selfAsActiveOperator >= 1;
    }

    private java.util.Set<AccountRole> parseRoleFilter(String role) {
        String normalized = role.trim().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
        if ("ALL".equals(normalized)) {
            return java.util.EnumSet.allOf(AccountRole.class);
        }
        try {
            return java.util.EnumSet.of(AccountRole.valueOf(normalized));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown role filter. Valid values: farmer, organization, personal_buyer, driver, operator, all");
        }
    }

    @Transactional
    public UserAccount changePassword(UserAccount account, String newPassword) {
        validatePassword(newPassword);
        if (passwordEncoder.matches(newPassword, account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Choose a password you have not used before");
        }
        account.setPasswordHash(passwordEncoder.encode(newPassword));
        account.setMustChangePassword(false);
        return accountRepository.save(account);
    }

    @Transactional
    public void createOperatorIfConfigured(String username, String password) {
        if (!StringUtils.hasText(username) && !StringUtils.hasText(password)) {
            return;
        }
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new IllegalStateException(
                    "Configure both APP_AUTH_OPERATOR_USERNAME and APP_AUTH_OPERATOR_PASSWORD to enable operator access");
        }
        if (username.trim().length() > 40) {
            throw new IllegalStateException("APP_AUTH_OPERATOR_USERNAME must be at most 40 characters");
        }
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > 72) {
            throw new IllegalStateException("APP_AUTH_OPERATOR_PASSWORD must contain 10 to 72 characters");
        }
        if (!accountRepository.existsByLoginIdIgnoreCase(username.trim())) {
            accountRepository.save(new UserAccount(
                    username.trim(),
                    passwordEncoder.encode(password),
                    "Marketplace Operator",
                    null,
                    null,
                    null,
                    AccountRole.OPERATOR,
                    false));
        }
    }

    private void ensureIdentityAvailable(String phone, String fanNumber, String tinNumber, String licenseNumber) {
        if (accountRepository.existsByPhoneNumber(phone)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account already uses this phone number");
        }
        if (StringUtils.hasText(fanNumber) && accountRepository.existsByFanNumberIgnoreCase(fanNumber)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This FAN / national ID is already registered");
        }
        if (StringUtils.hasText(tinNumber) && accountRepository.existsByTinNumberIgnoreCase(tinNumber)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This TIN / registration number is already registered");
        }
        if (StringUtils.hasText(licenseNumber) && accountRepository.existsByLicenseNumberIgnoreCase(licenseNumber)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This driver license number is already registered");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber) || !PHONE_PATTERN.matcher(normalizePhone(phoneNumber)).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter a valid phone number with country code");
        }
    }

    private String requireText(String value, int maxLength, String message) {
        if (!StringUtils.hasText(value) || value.trim().length() > maxLength) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        return value.trim();
    }

    private void validateFarmerDetails(String name, String fanNumber, String phoneNumber) {
        if (!StringUtils.hasText(name) || name.trim().length() > 120) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is required and must be at most 120 characters");
        }
        if (!StringUtils.hasText(fanNumber) || fanNumber.trim().length() > 80) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FAN / national ID is required and must be at most 80 characters");
        }
        if (!StringUtils.hasText(phoneNumber) || !PHONE_PATTERN.matcher(normalizePhone(phoneNumber)).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter a valid phone number with country code");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > 72) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Password must be between 10 and 72 characters");
        }
    }

    private String normalizePhone(String phone) {
        return phone == null ? "" : phone.replaceAll("[\\s()-]", "");
    }

    private String createFarmerSlug(String name) {
        String base = Normalizer.normalize(name.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (base.isBlank()) {
            base = "farmer";
        }
        return (base + "-" + UUID.randomUUID().toString().substring(0, 8)).substring(0,
                Math.min(base.length() + 9, 120));
    }

    private String createTemporaryPassword() {
        byte[] randomBytes = new byte[18];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public record RegistrationRequest(String role, String fullName, String fanNumber, String phoneNumber,
                                      String password, String organizationName, String tinNumber,
                                      String contactPersonName, String licenseNumber) {
    }

    public record RoleField(String name, String label, String type) {
    }

    public record RoleSpec(String role, String label, String description, List<RoleField> requiredFields) {
    }

    public record UserUpdateRequest(String displayName, String phoneNumber, String role,
                                    String organizationName, String contactPersonName) {
    }

    public record PageResult<T>(List<T> items, int page, int size, long totalItems, int totalPages) {
    }

    public record FarmerProvisionRequest(String fullName, String fanNumber, String phoneNumber) {
    }

    public record TemporaryAccount(UserAccount account, String temporaryPassword) {
    }
}
