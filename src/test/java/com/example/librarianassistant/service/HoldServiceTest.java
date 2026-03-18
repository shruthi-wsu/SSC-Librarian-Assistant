package com.example.librarianassistant.service;

import com.example.librarianassistant.dto.HoldResponse;
import com.example.librarianassistant.exception.BusinessException;
import com.example.librarianassistant.model.Book;
import com.example.librarianassistant.model.Hold;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.repository.BookRepository;
import com.example.librarianassistant.repository.HoldRepository;
import com.example.librarianassistant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class HoldServiceTest {

    @Autowired
    private HoldService holdService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private HoldRepository holdRepository;

    private User patron1;
    private User patron2;
    private Book book;

    @BeforeEach
    void setUp() {
        patron1 = userRepository.save(User.builder()
                .name("Patron One")
                .email("patron1@hold.test")
                .password("pass")
                .role(User.Role.PATRON)
                .build());

        patron2 = userRepository.save(User.builder()
                .name("Patron Two")
                .email("patron2@hold.test")
                .password("pass")
                .role(User.Role.PATRON)
                .build());

        book = bookRepository.save(Book.builder()
                .isbn("978-hold-001")
                .title("Hold Book")
                .author("Hold Author")
                .totalCopies(1)
                .availableCopies(0)
                .status(Book.BookStatus.CHECKED_OUT)
                .build());
    }

    @Test
    void placeHold_setsQueuePositionCorrectly() {
        HoldResponse h1 = holdService.placeHold(patron1.getId(), book.getId());
        HoldResponse h2 = holdService.placeHold(patron2.getId(), book.getId());

        assertThat(h1.getQueuePosition()).isEqualTo(1);
        assertThat(h2.getQueuePosition()).isEqualTo(2);
        assertThat(h1.getStatus()).isEqualTo(Hold.HoldStatus.PENDING);
    }

    @Test
    void placeHold_throwsOnDuplicate() {
        holdService.placeHold(patron1.getId(), book.getId());

        assertThatThrownBy(() -> holdService.placeHold(patron1.getId(), book.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("active hold");
    }

    @Test
    void cancelHold_setsStatusCancelled() {
        HoldResponse hold = holdService.placeHold(patron1.getId(), book.getId());

        holdService.cancelHold(hold.getId(), patron1.getEmail());

        Hold updated = holdRepository.findById(hold.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(Hold.HoldStatus.CANCELLED);
    }

    @Test
    void processNextHold_transitionsPendingToNotified() {
        HoldResponse h1 = holdService.placeHold(patron1.getId(), book.getId());
        holdService.placeHold(patron2.getId(), book.getId());

        holdService.processNextHold(book.getId());

        Hold updated = holdRepository.findById(h1.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(Hold.HoldStatus.NOTIFIED);
    }

    @Test
    void getUserHolds_returnsAllHoldsForUser() {
        holdService.placeHold(patron1.getId(), book.getId());

        assertThat(holdService.getUserHolds(patron1.getId())).hasSize(1);
        assertThat(holdService.getUserHolds(patron2.getId())).isEmpty();
    }
}
