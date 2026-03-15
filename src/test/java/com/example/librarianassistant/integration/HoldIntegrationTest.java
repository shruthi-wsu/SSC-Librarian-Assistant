package com.example.librarianassistant.integration;

import com.example.librarianassistant.dto.RegisterRequest;
import com.example.librarianassistant.model.Book;
import com.example.librarianassistant.model.Hold;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.repository.BookRepository;
import com.example.librarianassistant.repository.HoldRepository;
import com.example.librarianassistant.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class HoldIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private HoldRepository holdRepository;

    private MockMvc mockMvc;
    private String patron1Token;
    private Long patron1Id;
    private String patron2Token;
    private Long patron2Id;
    private Long bookId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        RegisterRequest reg1 = new RegisterRequest();
        reg1.setName("Hold Patron 1");
        reg1.setEmail("holdp1@test.com");
        reg1.setPassword("password123");
        reg1.setRole(User.Role.PATRON);
        var auth1 = userService.register(reg1);
        patron1Token = auth1.getToken();
        patron1Id = auth1.getUserId();

        RegisterRequest reg2 = new RegisterRequest();
        reg2.setName("Hold Patron 2");
        reg2.setEmail("holdp2@test.com");
        reg2.setPassword("password123");
        reg2.setRole(User.Role.PATRON);
        var auth2 = userService.register(reg2);
        patron2Token = auth2.getToken();
        patron2Id = auth2.getUserId();

        Book book = bookRepository.save(Book.builder()
                .isbn("978-hold-int-001")
                .title("Hold Integration Book")
                .author("Hold Author")
                .totalCopies(1)
                .availableCopies(0)
                .status(Book.BookStatus.CHECKED_OUT)
                .build());
        bookId = book.getId();
    }

    @Test
    void placeHold_thenCancel() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/holds")
                        .header("Authorization", "Bearer " + patron1Token)
                        .param("userId", patron1Id.toString())
                        .param("bookId", bookId.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn();

        Long holdId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(delete("/api/holds/" + holdId)
                        .header("Authorization", "Bearer " + patron1Token))
                .andExpect(status().isNoContent());

        Hold updated = holdRepository.findById(holdId).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(Hold.HoldStatus.CANCELLED);
    }

    @Test
    void duplicateHold_returns422() throws Exception {
        mockMvc.perform(post("/api/holds")
                        .header("Authorization", "Bearer " + patron1Token)
                        .param("userId", patron1Id.toString())
                        .param("bookId", bookId.toString()))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/holds")
                        .header("Authorization", "Bearer " + patron1Token)
                        .param("userId", patron1Id.toString())
                        .param("bookId", bookId.toString()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void placeHold_queueAdvancesAfterReturn() throws Exception {
        // Place hold for patron1
        MvcResult h1Result = mockMvc.perform(post("/api/holds")
                        .header("Authorization", "Bearer " + patron1Token)
                        .param("userId", patron1Id.toString())
                        .param("bookId", bookId.toString()))
                .andExpect(status().isCreated())
                .andReturn();
        Long h1Id = objectMapper.readTree(h1Result.getResponse().getContentAsString()).get("id").asLong();

        // Place hold for patron2
        mockMvc.perform(post("/api/holds")
                        .header("Authorization", "Bearer " + patron2Token)
                        .param("userId", patron2Id.toString())
                        .param("bookId", bookId.toString()))
                .andExpect(status().isCreated());

        // Make book available and create a checkout so we can return it
        Book book = bookRepository.findById(bookId).orElseThrow();
        book.setAvailableCopies(1);
        book.setStatus(Book.BookStatus.AVAILABLE);
        bookRepository.save(book);

        RegisterRequest checkoutUser = new RegisterRequest();
        checkoutUser.setName("Checkout User");
        checkoutUser.setEmail("couser@test.com");
        checkoutUser.setPassword("password123");
        checkoutUser.setRole(User.Role.PATRON);
        var coAuth = userService.register(checkoutUser);

        MvcResult coResult = mockMvc.perform(post("/api/checkouts")
                        .header("Authorization", "Bearer " + coAuth.getToken())
                        .param("userId", coAuth.getUserId().toString())
                        .param("bookId", bookId.toString()))
                .andExpect(status().isCreated())
                .andReturn();
        Long coId = objectMapper.readTree(coResult.getResponse().getContentAsString()).get("id").asLong();

        // Return the book - should trigger processNextHold
        mockMvc.perform(post("/api/checkouts/" + coId + "/return")
                        .header("Authorization", "Bearer " + coAuth.getToken()))
                .andExpect(status().isOk());

        // First hold should now be NOTIFIED
        Hold h1 = holdRepository.findById(h1Id).orElseThrow();
        assertThat(h1.getStatus()).isEqualTo(Hold.HoldStatus.NOTIFIED);
    }
}
