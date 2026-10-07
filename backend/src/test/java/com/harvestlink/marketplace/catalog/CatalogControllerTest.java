package com.harvestlink.marketplace.catalog;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import com.harvestlink.marketplace.config.SecurityConfiguration;
import com.harvestlink.marketplace.auth.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CatalogController.class)
@Import(SecurityConfiguration.class)
class CatalogControllerTest {

    @MockBean
    private UserAccountRepository userAccountRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductCategoryRepository productCategoryRepository;

    @Test
    void catalogEndpointReturnsCategoriesAndProducts() throws Exception {
        ProductCategory cereals = new ProductCategory("Cereals", "cereals", "Staple grains");
        Product product = new Product(
                "Teff",
                "teff",
                "Amhara Region",
                "kg",
                new BigDecimal("4200"),
                "ETB",
                180,
                "High-quality teff for injera and flour blending.",
                null,
                ProductStatus.AVAILABLE,
                cereals);

        when(productCategoryRepository.findAllByOrderByNameAsc()).thenReturn(List.of(cereals));
        when(productRepository.findAllByOrderByNameAsc()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/v1/catalog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories[0].slug").value("cereals"))
                .andExpect(jsonPath("$.products[0].name").value("Teff"))
                .andExpect(jsonPath("$.products[0].categoryName").value("Cereals"))
                .andExpect(jsonPath("$.products[0].price").value(4200));
    }
}
