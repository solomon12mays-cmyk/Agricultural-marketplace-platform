package com.harvestlink.marketplace.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class UserManagementIntegrationTest {

    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    private String operatorUsername = "op-usermgmt";
    private String operatorPassword = "OperatorPass2026!";

    @BeforeEach
    void seedOperator() {
        // Stable username keeps exactly one active operator across tests so the
        // last-operator guards have deterministic input.
        accountService.createOperatorIfConfigured(operatorUsername, operatorPassword);
    }

    private static String uniquePhone() {
        return "+2519" + String.format("%08d", Math.floorMod(SEQUENCE.incrementAndGet() * 4231, 100_000_000));
    }

    private MockHttpSession signInAsOperator() throws Exception {
        MvcResult login = mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"loginId":"%s","password":"%s"}
                                """.formatted(operatorUsername, operatorPassword)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("OPERATOR"))
                .andReturn();
        return (MockHttpSession) login.getRequest().getSession(false);
    }

    private void registerBuyer(String phone) throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "personal_buyer",
                                  "fullName": "Managed Buyer",
                                  "phoneNumber": "%s",
                                  "password": "BuyerPass2026!"
                                }
                                """.formatted(phone)))
                .andExpect(status().isOk());
    }

    private long findUserIdByPhone(String phone) throws Exception {
        MvcResult list = mockMvc.perform(get("/api/v1/operator/users")
                        .param("search", phone)
                        .session(signInAsOperator()))
                .andExpect(status().isOk())
                .andReturn();
        return ((Number) com.jayway.jsonpath.JsonPath
                .read(list.getResponse().getContentAsString(), "$.items[0].id")).longValue();
    }

    @Test
    void anonymousCannotListUsers() throws Exception {
        mockMvc.perform(get("/api/v1/operator/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void operatorCanListAndSearchUsersWithoutPasswordHashes() throws Exception {
        String phone = uniquePhone();
        registerBuyer(phone);

        mockMvc.perform(get("/api/v1/operator/users")
                        .param("search", phone)
                        .session(signInAsOperator()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].role").value("PERSONAL_BUYER"))
                .andExpect(jsonPath("$.items[0].active").value(true))
                .andExpect(jsonPath("$.items[0].loginId").value(phone))
                .andExpect(jsonPath("$.items[0].password").doesNotExist())
                .andExpect(jsonPath("$.items[0].passwordHash").doesNotExist())
                .andExpect(jsonPath("$.totalItems").value(1));
    }

    @Test
    void roleFilterNarrowsTheList() throws Exception {
        String phone = uniquePhone();
        registerBuyer(phone);

        mockMvc.perform(get("/api/v1/operator/users")
                        .param("role", "driver")
                        .session(signInAsOperator()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[?(@.role=='PERSONAL_BUYER')]").doesNotExist());

        mockMvc.perform(get("/api/v1/operator/users")
                        .param("role", "personal_buyer")
                        .param("search", phone)
                        .session(signInAsOperator()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].role").value("PERSONAL_BUYER"));
    }

    @Test
    void unknownUserReturns404() throws Exception {
        mockMvc.perform(get("/api/v1/operator/users/99999999")
                        .session(signInAsOperator()))
                .andExpect(status().isNotFound());
    }

    @Test
    void operatorCanUpdateDisplayNameRoleAndPhone() throws Exception {
        String phone = uniquePhone();
        registerBuyer(phone);
        long userId = findUserIdByPhone(phone);
        String newPhone = uniquePhone();

        mockMvc.perform(patch("/api/v1/operator/users/{id}", userId)
                        .with(csrf())
                        .session(signInAsOperator())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"displayName":"Renamed Buyer","phoneNumber":"%s","role":"driver"}
                                """.formatted(newPhone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("Renamed Buyer"))
                .andExpect(jsonPath("$.role").value("DRIVER"))
                .andExpect(jsonPath("$.phoneNumber").value(newPhone))
                // Phone-login accounts keep loginId in step with the phone number.
                .andExpect(jsonPath("$.loginId").value(newPhone));
    }

    @Test
    void duplicatePhoneUpdateIsRejected() throws Exception {
        String firstPhone = uniquePhone();
        String secondPhone = uniquePhone();
        registerBuyer(firstPhone);
        registerBuyer(secondPhone);
        long secondId = findUserIdByPhone(secondPhone);

        mockMvc.perform(patch("/api/v1/operator/users/{id}", secondId)
                        .with(csrf())
                        .session(signInAsOperator())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"phoneNumber":"%s"}
                                """.formatted(firstPhone)))
                .andExpect(status().isConflict());
    }

    @Test
    void deactivatedUserCannotSignInUntilReactivated() throws Exception {
        String phone = uniquePhone();
        registerBuyer(phone);
        long buyerId = findUserIdByPhone(phone);
        MockHttpSession operatorSession = signInAsOperator();

        mockMvc.perform(patch("/api/v1/operator/users/{id}/status", buyerId)
                        .with(csrf())
                        .session(operatorSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"loginId":"%s","password":"BuyerPass2026!"}
                                """.formatted(phone)))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(patch("/api/v1/operator/users/{id}/status", buyerId)
                        .with(csrf())
                        .session(operatorSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));

        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"loginId":"%s","password":"BuyerPass2026!"}
                                """.formatted(phone)))
                .andExpect(status().isOk());
    }

    @Test
    void operatorCannotDeactivateOwnAccount() throws Exception {
        MockHttpSession operatorSession = signInAsOperator();
        long operatorId = findOperatorId(operatorSession);

        mockMvc.perform(patch("/api/v1/operator/users/{id}/status", operatorId)
                        .with(csrf())
                        .session(operatorSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":false}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void lastActiveOperatorCannotBeDemoted() throws Exception {
        MockHttpSession operatorSession = signInAsOperator();
        long operatorId = findOperatorId(operatorSession);

        mockMvc.perform(patch("/api/v1/operator/users/{id}", operatorId)
                        .with(csrf())
                        .session(operatorSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"personal_buyer\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void resetPasswordIssuesTemporaryPasswordThatMustChange() throws Exception {
        String phone = uniquePhone();
        registerBuyer(phone);
        long buyerId = findUserIdByPhone(phone);
        MockHttpSession operatorSession = signInAsOperator();

        MvcResult reset = mockMvc.perform(post("/api/v1/operator/users/{id}/reset-password", buyerId)
                        .with(csrf())
                        .session(operatorSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mustChangePassword").value(true))
                .andReturn();
        String temporaryPassword = com.jayway.jsonpath.JsonPath
                .read(reset.getResponse().getContentAsString(), "$.temporaryPassword");

        MvcResult login = mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"loginId":"%s","password":"%s"}
                                """.formatted(phone, temporaryPassword)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mustChangePassword").value(true))
                .andReturn();

        MockHttpSession buyerSession = (MockHttpSession) login.getRequest().getSession(false);
        mockMvc.perform(post("/api/v1/auth/change-password")
                        .with(csrf())
                        .session(buyerSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newPassword\":\"NewBuyerPassword2026!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mustChangePassword").value(false));
    }

    private long findOperatorId(MockHttpSession session) throws Exception {
        MvcResult me = mockMvc.perform(get("/api/v1/auth/me").session(session))
                .andExpect(status().isOk())
                .andReturn();
        String loginId = com.jayway.jsonpath.JsonPath
                .read(me.getResponse().getContentAsString(), "$.loginId");
        return findUserIdByLoginId(loginId, session);
    }

    private long findUserIdByLoginId(String loginId, MockHttpSession session) throws Exception {
        MvcResult list = mockMvc.perform(get("/api/v1/operator/users")
                        .param("search", loginId)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
        return ((Number) com.jayway.jsonpath.JsonPath
                .read(list.getResponse().getContentAsString(), "$.items[0].id")).longValue();
    }
}
