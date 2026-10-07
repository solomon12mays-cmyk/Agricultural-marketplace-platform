package com.harvestlink.marketplace.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class AuthFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void anonymousUsersCannotProvisionFarmerAccounts() throws Exception {
        mockMvc.perform(post("/api/v1/operator/farmers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName":"Unauthorized Farmer",
                                  "fanNumber":"FAN-UNAUTHORIZED",
                                  "phoneNumber":"+251900000099"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void farmerCanRegisterSignInAndChangePassword() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String phone = "+2519" + String.format("%08d", Math.floorMod(System.nanoTime(), 100_000_000));
        String initialPassword = "FarmerPass2026!";

        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Abebe Bekele",
                                  "fanNumber": "FAN-%s",
                                  "phoneNumber": "%s",
                                  "password": "%s"
                                }
                                """.formatted(suffix, phone, initialPassword)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("FARMER"))
                .andExpect(jsonPath("$.mustChangePassword").value(false));

        MvcResult login = mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"loginId":"%s","password":"%s"}
                                """.formatted(phone, initialPassword)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("Abebe Bekele"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);
        mockMvc.perform(get("/api/v1/auth/me").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginId").value(phone));
    }

    @Test
    void operatorIssuedPasswordIsTemporaryAndCanBeChanged() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String phone = "+2519" + String.format("%08d", Math.floorMod(System.nanoTime(), 100_000_000));

        MvcResult provision = mockMvc.perform(post("/api/v1/operator/farmers")
                        .with(user("operator").roles("OPERATOR"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName":"Selamawit Hailu",
                                  "fanNumber":"FAN-OP-%s",
                                  "phoneNumber":"%s"
                                }
                                """.formatted(suffix, phone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mustChangePassword").value(true))
                .andReturn();

        String temporaryPassword = com.jayway.jsonpath.JsonPath
                .read(provision.getResponse().getContentAsString(), "$.temporaryPassword");
        UserAccount createdAccount = accountRepository.findByLoginIdIgnoreCase(phone).orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(
                passwordEncoder.matches(temporaryPassword, createdAccount.getPassword()));

        MvcResult login = mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"loginId":"%s","password":"%s"}
                                """.formatted(phone, temporaryPassword)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mustChangePassword").value(true))
                .andReturn();

        MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);
        mockMvc.perform(post("/api/v1/auth/change-password")
                        .session(session)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"newPassword":"NewFarmerPassword2026!"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mustChangePassword").value(false));
    }
}
