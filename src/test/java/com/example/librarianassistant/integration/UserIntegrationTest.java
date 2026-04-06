package com.example.librarianassistant.integration;

import com.example.librarianassistant.dto.RegisterRequest;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    private MockMvc mockMvc;
    private String librarianToken;
    private String patronToken;
    private Long patronId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        RegisterRequest libReg = new RegisterRequest();
        libReg.setName("User Librarian");
        libReg.setEmail("userlib@test.com");
        libReg.setPassword("password123");
        libReg.setRole(User.Role.LIBRARIAN);
        librarianToken = userService.register(libReg).getToken();

        RegisterRequest patReg = new RegisterRequest();
        patReg.setName("User Patron");
        patReg.setEmail("userpat@test.com");
        patReg.setPassword("password123");
        patReg.setRole(User.Role.PATRON);
        var patronAuth = userService.register(patReg);
        patronToken = patronAuth.getToken();
        patronId = patronAuth.getUserId();
    }

    @Test
    void getCurrentUser_returnsProfile() throws Exception {
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("userpat@test.com"))
                .andExpect(jsonPath("$.role").value("PATRON"));
    }

    @Test
    void getAllUsers_asLibrarian_returns200() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllUsers_asPatron_returns403() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUserById_asLibrarian_returns200() throws Exception {
        mockMvc.perform(get("/api/users/" + patronId)
                        .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patronId))
                .andExpect(jsonPath("$.email").value("userpat@test.com"));
    }

    @Test
    void getUserById_asPatron_returns403() throws Exception {
        mockMvc.perform(get("/api/users/" + patronId)
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateUserStatus_asLibrarian_suspends() throws Exception {
        mockMvc.perform(patch("/api/users/" + patronId + "/status")
                        .param("status", "SUSPENDED")
                        .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUSPENDED"));
    }

    @Test
    void updateUserStatus_asPatron_returns403() throws Exception {
        mockMvc.perform(patch("/api/users/" + patronId + "/status")
                        .param("status", "SUSPENDED")
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void getCurrentUser_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }
}
