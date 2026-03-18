package com.example.librarianassistant.service;

import com.example.librarianassistant.dto.FineResponse;
import com.example.librarianassistant.model.Book;
import com.example.librarianassistant.model.Checkout;
import com.example.librarianassistant.model.Fine;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.repository.BookRepository;
import com.example.librarianassistant.repository.CheckoutRepository;
import com.example.librarianassistant.repository.FineRepository;
import com.example.librarianassistant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FineServiceTest {

    @Autowired
    private FineService fineService;

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;

    private User patron;
    private Fine fine;

    @BeforeEach
    void setUp() {
        patron = userRepository.save(User.builder()
                .name("Fine Patron")
                .email("finep@test.com")
                .password("pass")
                .role(User.Role.PATRON)
                .build());

        Book book = bookRepository.save(Book.builder()
                .isbn("978-fine-001")
                .title("Fine Book")
                .author("Author")
                .totalCopies(1)
                .availableCopies(0)
                .build());

        Checkout checkout = checkoutRepository.save(Checkout.builder()
                .user(patron)
                .book(book)
                .dueDate(LocalDate.now().minusDays(3))
                .returnDate(LocalDate.now())
                .status(Checkout.CheckoutStatus.RETURNED)
                .fineAmount(new BigDecimal("0.75"))
                .build());

        fine = fineRepository.save(Fine.builder()
                .user(patron)
                .checkout(checkout)
                .amount(new BigDecimal("0.75"))
                .build());
    }

    @Test
    void payFine_setsStatusPaidAndPaidDate() {
        FineResponse response = fineService.payFine(fine.getId());

        assertThat(response.getStatus()).isEqualTo(Fine.FineStatus.PAID);
        assertThat(response.getPaidDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void waiveFine_setsStatusWaived() {
        FineResponse response = fineService.waiveFine(fine.getId());

        assertThat(response.getStatus()).isEqualTo(Fine.FineStatus.WAIVED);
    }

    @Test
    void getUserFines_returnsAllFinesForUser() {
        assertThat(fineService.getUserFines(patron.getId())).hasSize(1);
    }

    @Test
    void getUnpaidFines_returnsOnlyUnpaid() {
        assertThat(fineService.getUnpaidFines(patron.getId())).hasSize(1);

        fineService.payFine(fine.getId());

        assertThat(fineService.getUnpaidFines(patron.getId())).isEmpty();
    }

    @Test
    void getTotalUnpaid_returnsCorrectSum() {
        BigDecimal total = fineService.getTotalUnpaid(patron.getId());
        assertThat(total).isEqualByComparingTo(new BigDecimal("0.75"));
    }
}
