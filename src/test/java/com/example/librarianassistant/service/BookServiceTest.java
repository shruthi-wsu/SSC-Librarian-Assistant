package com.example.librarianassistant.service;

import com.example.librarianassistant.dto.BookRequest;
import com.example.librarianassistant.dto.BookResponse;
import com.example.librarianassistant.exception.BusinessException;
import com.example.librarianassistant.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    private BookRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new BookRequest();
        validRequest.setIsbn("978-0-book-001");
        validRequest.setTitle("Spring in Action");
        validRequest.setAuthor("Craig Walls");
        validRequest.setGenre("Technology");
        validRequest.setPublishYear(2022);
        validRequest.setTotalCopies(3);
        validRequest.setAvailableCopies(3);
        validRequest.setLocation("A1");
    }

    @Test
    void createBook_persistsAndReturnsResponse() {
        BookResponse response = bookService.createBook(validRequest);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getIsbn()).isEqualTo("978-0-book-001");
        assertThat(response.getTitle()).isEqualTo("Spring in Action");
    }

    @Test
    void createBook_throwsOnDuplicateIsbn() {
        bookService.createBook(validRequest);

        assertThatThrownBy(() -> bookService.createBook(validRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("ISBN already exists");
    }

    @Test
    void updateBook_changesFields() {
        BookResponse created = bookService.createBook(validRequest);

        BookRequest updateRequest = new BookRequest();
        updateRequest.setIsbn("978-0-book-001");
        updateRequest.setTitle("Updated Title");
        updateRequest.setAuthor("New Author");
        updateRequest.setTotalCopies(5);
        updateRequest.setAvailableCopies(5);

        BookResponse updated = bookService.updateBook(created.getId(), updateRequest);

        assertThat(updated.getTitle()).isEqualTo("Updated Title");
        assertThat(updated.getAuthor()).isEqualTo("New Author");
        assertThat(updated.getTotalCopies()).isEqualTo(5);
    }

    @Test
    void deleteBook_removesFromRepository() {
        BookResponse created = bookService.createBook(validRequest);
        bookService.deleteBook(created.getId());

        assertThat(bookRepository.findById(created.getId())).isEmpty();
    }

    @Test
    void searchBooks_returnsMatchingResults() {
        bookService.createBook(validRequest);

        BookRequest other = new BookRequest();
        other.setIsbn("978-0-other-002");
        other.setTitle("Clean Code");
        other.setAuthor("Robert Martin");
        other.setTotalCopies(2);
        other.setAvailableCopies(2);
        bookService.createBook(other);

        List<BookResponse> results = bookService.searchBooks("Spring");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Spring in Action");
    }
}
