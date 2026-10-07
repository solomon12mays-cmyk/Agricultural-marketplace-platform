package com.harvestlink.marketplace.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    OpenAPI marketplaceOpenApi() {
        return new OpenAPI().info(new Info()
                .title("HarvestLink Marketplace API")
                .version("v1")
                .description("REST API foundation for the Ethiopian Agricultural Marketplace."));
    }
}
