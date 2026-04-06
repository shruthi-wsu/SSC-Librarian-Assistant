package com.example.librarianassistant.integration;

import com.example.librarianassistant.dto.RegisterRequest;
import com.example.librarianassistant.model.Book;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.repository.BookRepository;
import com.example.librarianassistant.repository.FineRepository;
import com.example.librarianassistant.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.librarianassistant.model.Checkout;
import com.example.librarianassistant.repository.CheckoutRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CheckoutIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;

    private MockMvc mockMvc;
    private String librarianToken;
    private String patronToken;
    private Long patronId;
    private Long bookId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        RegisterRequest libReg = new RegisterRequest();
        libReg.setName("Checkout Librarian");
        libReg.setEmail("checkoutlib@test.com");
        libReg.setPassword("password123");
        libReg.setRole(User.Role.LIBRARIAN);
        librarianToken = userService.register(libReg).getToken();

        RegisterRequest reg = new RegisterRequest();
        reg.setName("Checkout Patron");
        reg.setEmail("checkout@test.com");
        reg.setPassword("password123");
        reg.setRole(User.Role.PATRON);
        var auth = userService.register(reg);
        patronToken = auth.getToken();
        patronId = auth.getUserId();

        Book book = bookRepository.save(Book.builder()
                .isbn("978-integration-001")
                .title("Integration Book")
                .author("Test Author")
                .totalCopies(1)
                .availableCopies(1)
                .build());
        bookId = book.getId();
    }

    @Test
    void checkoutAndReturn_fullFlow() throws Exception {
        // Checkout
        MvcResult checkoutResult = mockMvc.perform(post("/api/checkouts")
                        .header("Authorization", "Bearer " + patronToken)
                        .param("userId", patronId.toString())
                        .param("bookId", bookId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andReturn();

        String checkoutJson = checkoutResult.getResponse().getContentAsString();
        Long checkoutId = objectMapper.readTree(checkoutJson).get("id").asLong();

        // Return
        mockMvc.perform(post("/api/checkouts/" + checkoutId + "/return")
                        .header("Authorization", "Bearer " + patronToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RETURNED"));

        // Book availability restored
        Book updated = bookRepository.findById(bookId).orElseThrow();
        assertThat(updated.getAvailableCopies()).isEqualTo(1);
    }

    @Test
    void renewCheckout_withinLimit_extendsDueDate() throws Exception {
        // Checkout
        MvcResult checkoutResult = mockMvc.perform(post("/api/checkouts")
                        .header("Authorization", "Bearer " + patronToken)
                        .param("userId", patronId.toString())
                        .param("bookId", bookId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        Long checkoutId = objectMapper.readTree(checkoutResult.getResponse().getContentAsString()).get("id").asLong();

        // Renew
        mockMvc.perform(post("/api/checkouts/" + checkoutId + "/renew")
                        .header("Authorization", "Bearer " + patronToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.renewalCount").value(1))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void getOverdueCheckouts_asLibrarian_returns200() throws Exception {
        // Create a checkout and backdate dueDate to make it overdue
        MvcResult checkoutResult = mockMvc.perform(post("/api/checkouts")
                        .header("Authorization", "Bearer " + patronToken)
                        .param("userId", patronId.toString())
                        .param("bookId", bookId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        Long checkoutId = objectMapper.readTree(checkoutResult.getResponse().getContentAsString()).get("id").asLong();

        // Backdate the dueDate
        Checkout checkout = checkoutRepository.findById(checkoutId).orElseThrow();
        checkout.setDueDate(java.time.LocalDate.now().minusDays(5));
        checkoutRepository.save(checkout);

        // Librarian retrieves overdue checkouts
        mockMvc.perform(get("/api/checkouts/overdue")
                        .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(checkoutId));
    }

    @Test
    void checkoutSameBookTwice_returns422() throws Exception {
        // First checkout
        mockMvc.perform(post("/api/checkouts")
                        .header("Authorization", "Bearer " + patronToken)
                        .param("userId", patronId.toString())
                        .param("bookId", bookId.toString()))
                .andExpect(status().isCreated());

        // Second checkout - should fail
        mockMvc.perform(post("/api/checkouts")
                        .header("Authorization", "Bearer " + patronToken)
                        .param("userId", patronId.toString())
                        .param("bookId", bookId.toString()))
                .andExpect(status().isUnprocessableEntity());
    }
}
