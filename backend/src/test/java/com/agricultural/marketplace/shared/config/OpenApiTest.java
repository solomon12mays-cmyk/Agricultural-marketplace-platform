package com.agricultural.marketplace.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenApiTest {

    @Test
    @DisplayName("OpenAPI bean should configure metadata and Bearer JWT security scheme")
    void testOpenApiConfiguration() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.agriculturalMarketplaceOpenApi();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Ethiopian Agricultural Marketplace, Logistics & Payment API", openAPI.getInfo().getTitle());
        assertEquals("v1.0", openAPI.getInfo().getVersion());

        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getComponents().getSecuritySchemes());
        assertTrue(openAPI.getComponents().getSecuritySchemes().containsKey("BearerAuth"));
        assertEquals("JWT", openAPI.getComponents().getSecuritySchemes().get("BearerAuth").getBearerFormat());
    }
}

