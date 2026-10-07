package com.harvestlink.marketplace.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OperatorBootstrap {

    @Bean
    ApplicationRunner createConfiguredOperator(
            AccountService accountService,
            @Value("${app.auth.operator.username:}") String username,
            @Value("${app.auth.operator.password:}") String password) {
        return args -> accountService.createOperatorIfConfigured(username, password);
    }
}
