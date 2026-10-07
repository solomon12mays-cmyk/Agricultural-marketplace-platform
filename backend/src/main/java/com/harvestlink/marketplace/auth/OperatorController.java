package com.harvestlink.marketplace.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/operator/farmers")
public class OperatorController {

    private final AccountService accountService;

    public OperatorController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ProvisionedFarmerResponse provisionFarmer(
            @RequestBody AccountService.FarmerProvisionRequest request) {
        AccountService.TemporaryAccount account = accountService.provisionFarmer(request);
        UserAccount user = account.account();
        return new ProvisionedFarmerResponse(
                user.getDisplayName(),
                user.getPhoneNumber(),
                user.getFanNumber(),
                account.temporaryPassword(),
                true);
    }

    public record ProvisionedFarmerResponse(String fullName, String phoneNumber, String fanNumber,
                                            String temporaryPassword, boolean mustChangePassword) {
    }
}
