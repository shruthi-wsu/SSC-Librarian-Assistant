package com.example.librarianassistant.service;

import com.example.librarianassistant.dto.CheckoutResponse;
import com.example.librarianassistant.model.Book;
import com.example.librarianassistant.model.Checkout;
import com.example.librarianassistant.model.Fine;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.repository.BookRepository;
import com.example.librarianassistant.repository.CheckoutRepository;
import com.example.librarianassistant.repository.FineRepository;
import com.example.librarianassistant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CirculationService {

    private static final int LOAN_PERIOD_DAYS = 14;
    private static final int MAX_RENEWALS = 2;
    private static final BigDecimal FINE_PER_DAY = new BigDecimal("0.25");

    private final CheckoutRepository checkoutRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final FineRepository fineRepository;

    @Transactional
    public CheckoutResponse checkoutBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies for book: " + bookId);
        }
        if (checkoutRepository.findByUserIdAndBookIdAndStatus(userId, bookId, Checkout.CheckoutStatus.ACTIVE).isPresent()) {
            throw new IllegalStateException("User already has this book checked out");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        if (book.getAvailableCopies() == 0) {
            book.setStatus(Book.BookStatus.CHECKED_OUT);
        }
        bookRepository.save(book);

        Checkout checkout = Checkout.builder()
                .user(user)
                .book(book)
                .dueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS))
                .build();
        return toResponse(checkoutRepository.save(checkout));
    }

    @Transactional
    public CheckoutResponse returnBook(Long checkoutId) {
        Checkout checkout = checkoutRepository.findById(checkoutId)
                .orElseThrow(() -> new IllegalArgumentException("Checkout not found: " + checkoutId));

        if (checkout.getStatus() == Checkout.CheckoutStatus.RETURNED) {
            throw new IllegalStateException("Book already returned");
        }

        LocalDate returnDate = LocalDate.now();
        checkout.setReturnDate(returnDate);
        checkout.setStatus(Checkout.CheckoutStatus.RETURNED);

        if (returnDate.isAfter(checkout.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(checkout.getDueDate(), returnDate);
            BigDecimal fineAmount = FINE_PER_DAY.multiply(BigDecimal.valueOf(daysOverdue));
            checkout.setFineAmount(fineAmount);

            Fine fine = Fine.builder()
                    .user(checkout.getUser())
                    .checkout(checkout)
                    .amount(fineAmount)
                    .build();
            fineRepository.save(fine);
        }

        Book book = checkout.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        if (book.getStatus() == Book.BookStatus.CHECKED_OUT) {
            book.setStatus(Book.BookStatus.AVAILABLE);
        }
        bookRepository.save(book);

        return toResponse(checkoutRepository.save(checkout));
    }

    @Transactional
    public CheckoutResponse renewCheckout(Long checkoutId) {
        Checkout checkout = checkoutRepository.findById(checkoutId)
                .orElseThrow(() -> new IllegalArgumentException("Checkout not found: " + checkoutId));

        if (checkout.getStatus() != Checkout.CheckoutStatus.ACTIVE) {
            throw new IllegalStateException("Only active checkouts can be renewed");
        }
        if (checkout.getRenewalCount() >= MAX_RENEWALS) {
            throw new IllegalStateException("Maximum renewals (" + MAX_RENEWALS + ") reached");
        }

        checkout.setDueDate(checkout.getDueDate().plusDays(LOAN_PERIOD_DAYS));
        checkout.setRenewalCount(checkout.getRenewalCount() + 1);
        return toResponse(checkoutRepository.save(checkout));
    }

    public List<CheckoutResponse> getUserCheckouts(Long userId) {
        return checkoutRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CheckoutResponse> getOverdueCheckouts() {
        return checkoutRepository.findOverdue(LocalDate.now()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CheckoutResponse toResponse(Checkout c) {
        return CheckoutResponse.builder()
                .id(c.getId())
                .userId(c.getUser().getId())
                .userName(c.getUser().getName())
                .bookId(c.getBook().getId())
                .bookTitle(c.getBook().getTitle())
                .bookIsbn(c.getBook().getIsbn())
                .checkoutDate(c.getCheckoutDate())
                .dueDate(c.getDueDate())
                .returnDate(c.getReturnDate())
                .renewalCount(c.getRenewalCount())
                .fineAmount(c.getFineAmount())
                .status(c.getStatus())
                .build();
    }
}
