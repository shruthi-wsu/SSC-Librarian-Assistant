package com.example.librarianassistant.service;

import com.example.librarianassistant.dto.CheckoutResponse;
import com.example.librarianassistant.exception.BusinessException;
import com.example.librarianassistant.model.Book;
import com.example.librarianassistant.model.Checkout;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.repository.BookRepository;
import com.example.librarianassistant.repository.CheckoutRepository;
import com.example.librarianassistant.repository.FineRepository;
import com.example.librarianassistant.repository.HoldRepository;
import com.example.librarianassistant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CirculationServiceTest {

    @Autowired
    private CirculationService circulationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private HoldRepository holdRepository;

    private User patron;
    private Book book;

    @BeforeEach
    void setUp() {
        patron = userRepository.save(User.builder()
                .name("Test Patron")
                .email("patron@test.com")
                .password("pass")
                .role(User.Role.PATRON)
                .build());

        book = bookRepository.save(Book.builder()
                .isbn("978-0-test-001")
                .title("Test Book")
                .author("Test Author")
                .totalCopies(2)
                .availableCopies(2)
                .build());
    }

    @Test
    void checkoutBook_decrementsCopies() {
        CheckoutResponse response = circulationService.checkoutBook(patron.getId(), book.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(Checkout.CheckoutStatus.ACTIVE);

        Book updated = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(updated.getAvailableCopies()).isEqualTo(1);
    }

    @Test
    void checkoutBook_setsBookStatusCheckedOut_whenLastCopy() {
        // checkout first copy
        circulationService.checkoutBook(patron.getId(), book.getId());
        // change book to 1 copy so next checkout exhausts it
        book = bookRepository.findById(book.getId()).orElseThrow();

        User patron2 = userRepository.save(User.builder()
                .name("Patron 2")
                .email("patron2@test.com")
                .password("pass")
                .role(User.Role.PATRON)
                .build());

        circulationService.checkoutBook(patron2.getId(), book.getId());

        Book updated = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(updated.getAvailableCopies()).isEqualTo(0);
        assertThat(updated.getStatus()).isEqualTo(Book.BookStatus.CHECKED_OUT);
    }

    @Test
    void checkoutBook_throwsWhenNoCopiesAvailable() {
        // Use a book with 0 copies
        Book nocopies = bookRepository.save(Book.builder()
                .isbn("978-0-nocopies")
                .title("No Copies")
                .author("Author")
                .totalCopies(0)
                .availableCopies(0)
                .build());

        assertThatThrownBy(() -> circulationService.checkoutBook(patron.getId(), nocopies.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("No available copies");
    }

    @Test
    void returnBook_generatesFinewhenOverdue() {
        CheckoutResponse checkout = circulationService.checkoutBook(patron.getId(), book.getId());

        // Manually back-date the due date to simulate overdue
        Checkout co = checkoutRepository.findById(checkout.getId()).orElseThrow();
        co.setDueDate(LocalDate.now().minusDays(5));
        checkoutRepository.save(co);

        CheckoutResponse returned = circulationService.returnBook(checkout.getId());

        assertThat(returned.getStatus()).isEqualTo(Checkout.CheckoutStatus.RETURNED);
        assertThat(returned.getFineAmount()).isPositive();
        assertThat(fineRepository.findByUserId(patron.getId())).hasSize(1);
    }

    @Test
    void returnBook_noFine_whenOnTime() {
        CheckoutResponse checkout = circulationService.checkoutBook(patron.getId(), book.getId());
        CheckoutResponse returned = circulationService.returnBook(checkout.getId());

        assertThat(returned.getStatus()).isEqualTo(Checkout.CheckoutStatus.RETURNED);
        assertThat(fineRepository.findByUserId(patron.getId())).isEmpty();
    }

    @Test
    void renewCheckout_extendsDueDateAndIncrementsRenewalCount() {
        CheckoutResponse checkout = circulationService.checkoutBook(patron.getId(), book.getId());
        LocalDate originalDue = checkout.getDueDate();

        CheckoutResponse renewed = circulationService.renewCheckout(checkout.getId());

        assertThat(renewed.getDueDate()).isEqualTo(originalDue.plusDays(14));
        assertThat(renewed.getRenewalCount()).isEqualTo(1);
    }

    @Test
    void renewCheckout_throwsWhenMaxRenewalsReached() {
        CheckoutResponse checkout = circulationService.checkoutBook(patron.getId(), book.getId());
        circulationService.renewCheckout(checkout.getId());
        circulationService.renewCheckout(checkout.getId());

        assertThatThrownBy(() -> circulationService.renewCheckout(checkout.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Maximum renewals");
    }
}
