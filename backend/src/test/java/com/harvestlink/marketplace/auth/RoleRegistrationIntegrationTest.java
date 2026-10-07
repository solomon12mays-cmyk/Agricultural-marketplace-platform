package com.harvestlink.marketplace.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RoleRegistrationIntegrationTest {

    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    @Autowired
    private MockMvc mockMvc;

    private static String uniquePhone() {
        return "+2519" + String.format("%08d", Math.floorMod(SEQUENCE.incrementAndGet() * 3571, 100_000_000));
    }

    @Test
    void rolesMetadataListsEveryRegistrationRoleWithFields() throws Exception {
        mockMvc.perform(get("/api/v1/auth/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].role").value("FARMER"))
                .andExpect(jsonPath("$[0].requiredFields[?(@.name=='fanNumber')]").exists())
                .andExpect(jsonPath("$[1].role").value("ORGANIZATION"))
                .andExpect(jsonPath("$[1].requiredFields[?(@.name=='tinNumber')]").exists())
                .andExpect(jsonPath("$[1].requiredFields[?(@.name=='organizationName')]").exists())
                .andExpect(jsonPath("$[2].role").value("PERSONAL_BUYER"))
                .andExpect(jsonPath("$[2].requiredFields[?(@.name=='fullName')]").exists())
                .andExpect(jsonPath("$[3].role").value("DRIVER"))
                .andExpect(jsonPath("$[3].requiredFields[?(@.name=='licenseNumber')]").exists());
    }

    @Test
    void organizationRegistersWithTinAndContactPerson() throws Exception {
        String phone = uniquePhone();

        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "ORGANIZATION",
                                  "organizationName": "Awash Agro Union",
                                  "tinNumber": "TIN-%d",
                                  "contactPersonName": "Mulugeta Assefa",
                                  "phoneNumber": "%s",
                                  "password": "UnionPass2026!"
                                }
                                """.formatted(SEQUENCE.incrementAndGet(), phone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ORGANIZATION"))
                .andExpect(jsonPath("$.displayName").value("Awash Agro Union"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"loginId":"%s","password":"UnionPass2026!"}
                                """.formatted(phone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ORGANIZATION"));
    }

    @Test
    void organizationRegistrationRequiresTin() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "organization",
                                  "organizationName": "Missing TIN Coop",
                                  "contactPersonName": "Someone",
                                  "phoneNumber": "%s",
                                  "password": "UnionPass2026!"
                                }
                                """.formatted(uniquePhone())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void personalBuyerRegistersWithMinimalFields() throws Exception {
        String phone = uniquePhone();

        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "personal_buyer",
                                  "fullName": "Hanna Girma",
                                  "phoneNumber": "%s",
                                  "password": "BuyerPass2026!"
                                }
                                """.formatted(phone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("PERSONAL_BUYER"))
                .andExpect(jsonPath("$.displayName").value("Hanna Girma"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"loginId":"%s","password":"BuyerPass2026!"}
                                """.formatted(phone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("PERSONAL_BUYER"));
    }

    @Test
    void driverRegistersWithLicenseNumber() throws Exception {
        String phone = uniquePhone();

        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "driver",
                                  "fullName": "Tesfaye Wolde",
                                  "fanNumber": "FAN-D-%d",
                                  "licenseNumber": "DL-%d",
                                  "phoneNumber": "%s",
                                  "password": "DriverPass2026!"
                                }
                                """.formatted(SEQUENCE.incrementAndGet(), SEQUENCE.incrementAndGet(), phone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("DRIVER"));
    }

    @Test
    void driverRegistrationRequiresLicenseNumber() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "DRIVER",
                                  "fullName": "No License",
                                  "fanNumber": "FAN-DL-MISSING-%d",
                                  "phoneNumber": "%s",
                                  "password": "DriverPass2026!"
                                }
                                """.formatted(SEQUENCE.incrementAndGet(), uniquePhone())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unknownRoleIsRejected() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "astronaut",
                                  "fullName": "Curious Person",
                                  "phoneNumber": "%s",
                                  "password": "SomePass2026!"
                                }
                                """.formatted(uniquePhone())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void duplicatePhoneIsRejectedAcrossRoles() throws Exception {
        String phone = uniquePhone();

        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "personal_buyer",
                                  "fullName": "First Buyer",
                                  "phoneNumber": "%s",
                                  "password": "BuyerPass2026!"
                                }
                                """.formatted(phone)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "driver",
                                  "fullName": "Second Driver",
                                  "fanNumber": "FAN-DUP-%d",
                                  "licenseNumber": "DL-DUP-%d",
                                  "phoneNumber": "%s",
                                  "password": "DriverPass2026!"
                                }
                                """.formatted(SEQUENCE.incrementAndGet(), SEQUENCE.incrementAndGet(), phone)))
                .andExpect(status().isConflict());
    }

    @Test
    void registrationWithoutRoleDefaultsToFarmer() throws Exception {
        String phone = uniquePhone();

        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Legacy Farmer",
                                  "fanNumber": "FAN-LEGACY-%d",
                                  "phoneNumber": "%s",
                                  "password": "FarmerPass2026!"
                                }
                                """.formatted(SEQUENCE.incrementAndGet(), phone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("FARMER"));
    }
}
