package com.agricultural.marketplace.shared.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger UI documentation configuration.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI agriculturalMarketplaceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ethiopian Agricultural Marketplace, Logistics & Payment API")
                        .description("Authoritative REST API specification for Agricultural Commerce, Multi-channel Identity, "
                                + "Inventory Ledger, Logistics Tracking, Buyer Prepayment Escrow and Platform Settlement.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Platform Engineering Team")
                                .email("api-support@harvestlink.et"))
                        .license(new License()
                                .name("Confidential & Proprietary")
                                .url("https://harvestlink.et/terms")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT Bearer token issued by /api/v1/auth/login")));
    }
}
