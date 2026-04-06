package com.example.librarianassistant.integration;

import com.example.librarianassistant.dto.RegisterRequest;
import com.example.librarianassistant.model.Book;
import com.example.librarianassistant.model.Checkout;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.repository.BookRepository;
import com.example.librarianassistant.repository.CheckoutRepository;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for FineController.
 *
 * <p>Setup: creates a checkout with a backdated dueDate (10 days ago) and calls
 * the return endpoint to trigger automatic fine generation via CirculationService.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FineIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private MockMvc mockMvc;
    private String librarianToken;
    private String patronToken;
    private Long patronId;
    private Long fineId;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        RegisterRequest libReg = new RegisterRequest();
        libReg.setName("Fine Librarian");
        libReg.setEmail("finelib@test.com");
        libReg.setPassword("password123");
        libReg.setRole(User.Role.LIBRARIAN);
        librarianToken = userService.register(libReg).getToken();

        RegisterRequest patReg = new RegisterRequest();
        patReg.setName("Fine Patron");
        patReg.setEmail("finepat@test.com");
        patReg.setPassword("password123");
        patReg.setRole(User.Role.PATRON);
        var patronAuth = userService.register(patReg);
        patronToken = patronAuth.getToken();
        patronId = patronAuth.getUserId();

        // Seed a book
        Book book = bookRepository.save(Book.builder()
                .isbn("978-fine-int-001")
                .title("Fine Test Book")
                .author("Test Author")
                .totalCopies(1)
                .availableCopies(1)
                .build());

        // Create a checkout and backdate dueDate to 10 days ago to ensure overdue
        MvcResult checkoutResult = mockMvc.perform(post("/api/checkouts")
                        .header("Authorization", "Bearer " + patronToken)
                        .param("userId", patronId.toString())
                        .param("bookId", book.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        Long checkoutId = objectMapper.readTree(checkoutResult.getResponse().getContentAsString()).get("id").asLong();

        // Backdate dueDate so the return triggers a fine
        Checkout checkout = checkoutRepository.findById(checkoutId).orElseThrow();
        checkout.setDueDate(LocalDate.now().minusDays(10));
        checkoutRepository.save(checkout);

        // Return the overdue book — this generates a fine
        mockMvc.perform(post("/api/checkouts/" + checkoutId + "/return")
                        .header("Authorization", "Bearer " + patronToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Retrieve the fine ID
        MvcResult fineResult = mockMvc.perform(get("/api/fines/user/" + patronId)
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isOk())
                .andReturn();
        fineId = objectMapper.readTree(fineResult.getResponse().getContentAsString()).get(0).get("id").asLong();
    }

    @Test
    void getUserFines_returnsFineList() throws Exception {
        mockMvc.perform(get("/api/fines/user/" + patronId)
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("UNPAID"))
                .andExpect(jsonPath("$[0].amount").isNumber());
    }

    @Test
    void getUnpaidFines_returnsUnpaidOnly() throws Exception {
        mockMvc.perform(get("/api/fines/user/" + patronId + "/unpaid")
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("UNPAID"));
    }

    @Test
    void payFine_marksAsPaid() throws Exception {
        mockMvc.perform(post("/api/fines/" + fineId + "/pay")
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    void waiveFine_asLibrarian_marksAsWaived() throws Exception {
        mockMvc.perform(post("/api/fines/" + fineId + "/waive")
                        .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("WAIVED"));
    }

    @Test
    void waiveFine_asPatron_returns403() throws Exception {
        mockMvc.perform(post("/api/fines/" + fineId + "/waive")
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isForbidden());
    }
}
