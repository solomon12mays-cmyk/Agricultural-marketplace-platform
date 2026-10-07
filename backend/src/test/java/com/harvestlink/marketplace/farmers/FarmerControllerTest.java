package com.harvestlink.marketplace.farmers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.harvestlink.marketplace.config.SecurityConfiguration;
import com.harvestlink.marketplace.auth.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FarmerController.class)
@Import(SecurityConfiguration.class)
class FarmerControllerTest {

    @MockBean
    private UserAccountRepository userAccountRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FarmerRepository farmerRepository;

    @Test
    void farmerListEndpointReturnsProfiles() throws Exception {
        Farmer farmer = new Farmer(
                "Alemu Bekele",
                "alemu-bekele",
                "Amhara",
                "Teff and maize",
                "18 hectares",
                4.8,
                "Community-focused grower",
                "Alemu Bekele",
                "+251911000111",
                14);

        when(farmerRepository.findAllByOrderByNameAsc()).thenReturn(List.of(farmer));

        mockMvc.perform(get("/api/v1/farmers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alemu Bekele"))
                .andExpect(jsonPath("$[0].cropFocus").value("Teff and maize"));
    }

    @Test
    void farmerProfileEndpointAllowsUpdates() throws Exception {
        Farmer farmer = new Farmer(
                "Alemu Bekele",
                "alemu-bekele",
                "Amhara",
                "Teff and maize",
                "18 hectares",
                4.8,
                "Community-focused grower",
                "Alemu Bekele",
                "+251911000111",
                14);

        when(farmerRepository.findBySlugIgnoreCase("alemu-bekele")).thenReturn(java.util.Optional.of(farmer));
        when(farmerRepository.save(any(Farmer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(patch("/api/v1/farmers/alemu-bekele/profile")
                        .contentType("application/json")
                        .content("{\"region\":\"Oromia\",\"rating\":4.9}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.region").value("Oromia"))
                .andExpect(jsonPath("$.rating").value(4.9));
    }
}
