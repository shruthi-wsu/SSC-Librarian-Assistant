package com.example.librarianassistant.integration;

import com.example.librarianassistant.dto.BookRequest;
import com.example.librarianassistant.dto.RegisterRequest;
import com.example.librarianassistant.model.Book;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.repository.BookRepository;
import com.example.librarianassistant.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private String librarianToken;
    private String patronToken;
    private Long seededBookId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        RegisterRequest libReg = new RegisterRequest();
        libReg.setName("Book Librarian");
        libReg.setEmail("booklib@test.com");
        libReg.setPassword("password123");
        libReg.setRole(User.Role.LIBRARIAN);
        librarianToken = userService.register(libReg).getToken();

        RegisterRequest patReg = new RegisterRequest();
        patReg.setName("Book Patron");
        patReg.setEmail("bookpat@test.com");
        patReg.setPassword("password123");
        patReg.setRole(User.Role.PATRON);
        patronToken = userService.register(patReg).getToken();

        Book book = bookRepository.save(Book.builder()
                .isbn("978-book-int-001")
                .title("Spring in Action")
                .author("Craig Walls")
                .genre("Technology")
                .publishYear(2022)
                .totalCopies(3)
                .availableCopies(3)
                .build());
        seededBookId = book.getId();
    }

    @Test
    void getAllBooks_returnsSeededBook() throws Exception {
        // DataSeeder adds books on startup; verify our test book is present anywhere in the list
        mockMvc.perform(get("/api/books")
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].title", hasItem("Spring in Action")));
    }

    @Test
    void getBookById_found() throws Exception {
        mockMvc.perform(get("/api/books/" + seededBookId)
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("978-book-int-001"))
                .andExpect(jsonPath("$.author").value("Craig Walls"));
    }

    @Test
    void getBookById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/books/99999")
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchBooks_byTitle_returnsMatches() throws Exception {
        mockMvc.perform(get("/api/books/search")
                        .param("query", "Spring")
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Spring in Action"));
    }

    @Test
    void searchBooks_noMatches_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/books/search")
                        .param("query", "zzz9999nomatch")
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createBook_asLibrarian_returns201() throws Exception {
        BookRequest request = new BookRequest();
        request.setIsbn("978-new-book-001");
        request.setTitle("Clean Code");
        request.setAuthor("Robert C. Martin");
        request.setTotalCopies(2);
        request.setAvailableCopies(2);

        mockMvc.perform(post("/api/books")
                        .header("Authorization", "Bearer " + librarianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value("978-new-book-001"))
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    void createBook_asPatron_returns403() throws Exception {
        BookRequest request = new BookRequest();
        request.setIsbn("978-patron-denied-001");
        request.setTitle("Denied Book");
        request.setAuthor("Test Author");
        request.setTotalCopies(1);
        request.setAvailableCopies(1);

        mockMvc.perform(post("/api/books")
                        .header("Authorization", "Bearer " + patronToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createBook_duplicateIsbn_returns422() throws Exception {
        BookRequest request = new BookRequest();
        request.setIsbn("978-book-int-001"); // same as seeded book
        request.setTitle("Duplicate ISBN Book");
        request.setAuthor("Author");
        request.setTotalCopies(1);
        request.setAvailableCopies(1);

        mockMvc.perform(post("/api/books")
                        .header("Authorization", "Bearer " + librarianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateBook_asLibrarian_returns200() throws Exception {
        BookRequest request = new BookRequest();
        request.setIsbn("978-book-int-001");
        request.setTitle("Spring in Action – Updated");
        request.setAuthor("Craig Walls");
        request.setTotalCopies(5);
        request.setAvailableCopies(5);

        mockMvc.perform(put("/api/books/" + seededBookId)
                        .header("Authorization", "Bearer " + librarianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring in Action – Updated"))
                .andExpect(jsonPath("$.totalCopies").value(5));
    }

    @Test
    void deleteBook_asLibrarian_returns204() throws Exception {
        mockMvc.perform(delete("/api/books/" + seededBookId)
                        .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isNoContent());

        // Verify deleted
        mockMvc.perform(get("/api/books/" + seededBookId)
                        .header("Authorization", "Bearer " + patronToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBooks_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized());
    }
}
