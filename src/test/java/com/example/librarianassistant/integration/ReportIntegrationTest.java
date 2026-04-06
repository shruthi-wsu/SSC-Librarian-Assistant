package com.example.librarianassistant.integration;

import com.example.librarianassistant.dto.RegisterRequest;
import com.example.librarianassistant.model.Book;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.repository.BookRepository;
import com.example.librarianassistant.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReportIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepository;

    private MockMvc mockMvc;
    private String librarianToken;
    private String patronToken;
    private Long patronId;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        RegisterRequest libReg = new RegisterRequest();
        libReg.setName("Report Librarian");
        libReg.setEmail("reportlib@test.com");
        libReg.setPassword("password123");
        libReg.setRole(User.Role.LIBRARIAN);
        librarianToken = userService.register(libReg).getToken();

        RegisterRequest patReg = new RegisterRequest();
        patReg.setName("Report Patron");
        patReg.setEmail("reportpat@test.com");
        patReg.setPassword("password123");
        patReg.setRole(User.Role.PATRON);
        var patronAuth = userService.register(patReg);
        patronToken = patronAuth.getToken();
        patronId = patronAuth.getUserId();

        // Seed a book and perform a checkout to populate report data
        Book book = bookRepository.save(Book.builder()
                .isbn("978-report-int-001")
                .title("Report Test Book")
                .author("Report Author")
                .totalCopies(2)
                .availableCopies(2)
                .build());

        mockMvc.perform(post("/api/checkouts")
                        .header("Authorization", "Bearer " + patronToken)
                        .param("userId", patronId.toString())
                        .param("bookId", book.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void getCirculationReport_asLibrarian_returns200() throws Exception {
        String from = LocalDate.now().minusMonths(1).toString();
        String to = LocalDate.now().plusDays(1).toString();

        mockMvc.perform(get("/api/reports/circulation")
                        .param("from", from)
                        .param("to", to)
                        .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCheckouts").isNumber())
                .andExpect(jsonPath("$.activeCheckouts").isNumber())
                .andExpect(jsonPath("$.periodFrom").isNotEmpty());
    }

    @Test
    void getOverdueReport_asLibrarian_returns200() throws Exception {
        mockMvc.perform(get("/api/reports/overdue")
                        .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isOk())
                // May be empty array if no items are overdue — just verify it's an array
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getPopularBooks_asLibrarian_returns200() throws Exception {
        mockMvc.perform(get("/api/reports/popular-books")
                        .param("limit", "5")
                        .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Report Test Book"));
    }

    @Test
    void getCirculationReport_asPatron_returns403() throws Exception {
        String from = LocalDate.now().minusMonths(1).toString();
        String to = LocalDate.now().plusDays(1).toString();

        mockMvc.perform(get("/api/reports/circulation")
                        .param("from", from)
                        .param("to", to)
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void getPopularBooks_asPatron_returns403() throws Exception {
        mockMvc.perform(get("/api/reports/popular-books")
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isForbidden());
    }
}
