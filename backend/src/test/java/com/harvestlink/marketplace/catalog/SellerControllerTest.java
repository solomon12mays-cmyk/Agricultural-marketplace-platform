package com.harvestlink.marketplace.catalog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

@WebMvcTest(SellerController.class)
@Import(SecurityConfiguration.class)
class SellerControllerTest {

    @MockBean
    private UserAccountRepository userAccountRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SellerRepository sellerRepository;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void sellerDetailEndpointReturnsProfileAndProducts() throws Exception {
        ProductCategory cereals = new ProductCategory("Cereals", "cereals", "Staple grains");
        Seller seller = new Seller(
                "Aster Cooperative",
                "aster-cooperative",
                "Addis Ababa",
                "A community-focused supplier of staple grains and produce.",
                4.8);

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
        product.setSeller(seller);
        seller.getProducts().add(product);

        when(sellerRepository.findAllByOrderByNameAsc()).thenReturn(List.of(seller));

        mockMvc.perform(get("/api/v1/sellers/aster-cooperative"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Aster Cooperative"))
                .andExpect(jsonPath("$.productCount").value(1))
                .andExpect(jsonPath("$.products[0].name").value("Teff"));
    }

    @Test
    void inventoryEndpointReturnsLowStockStatusAndAllowsStockUpdates() throws Exception {
        ProductCategory cereals = new ProductCategory("Cereals", "cereals", "Staple grains");
        Seller seller = new Seller(
                "Aster Cooperative",
                "aster-cooperative",
                "Addis Ababa",
                "A community-focused supplier of staple grains and produce.",
                4.8);

        Product product = new Product(
                "Teff",
                "teff",
                "Amhara Region",
                "kg",
                new BigDecimal("4200"),
                "ETB",
                12,
                "High-quality teff for injera and flour blending.",
                null,
                ProductStatus.AVAILABLE,
                cereals);
        product.setSeller(seller);
        seller.getProducts().add(product);

        when(sellerRepository.findAllByOrderByNameAsc()).thenReturn(List.of(seller));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(get("/api/v1/sellers/aster-cooperative/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Teff"))
                .andExpect(jsonPath("$[0].lowStock").value(true));

        mockMvc.perform(patch("/api/v1/sellers/aster-cooperative/inventory/teff")
                        .contentType("application/json")
                        .content("{\"delta\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity").value(22))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void sellerProfileEndpointAllowsSelectiveUpdates() throws Exception {
        ProductCategory cereals = new ProductCategory("Cereals", "cereals", "Staple grains");
        Seller seller = new Seller(
                "Aster Cooperative",
                "aster-cooperative",
                "Addis Ababa",
                "A community-focused supplier of staple grains and produce.",
                4.8);

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
        product.setSeller(seller);
        seller.getProducts().add(product);

        when(sellerRepository.findAllByOrderByNameAsc()).thenReturn(List.of(seller));
        when(sellerRepository.save(any(Seller.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(patch("/api/v1/sellers/aster-cooperative/profile")
                        .contentType("application/json")
                        .content("{\"location\":\"Dire Dawa\",\"rating\":4.9}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Dire Dawa"))
                .andExpect(jsonPath("$.rating").value(4.9));
    }
}
