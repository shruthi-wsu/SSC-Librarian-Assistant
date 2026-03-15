package com.example.librarianassistant.service;

import com.example.librarianassistant.dto.CirculationReportResponse;
import com.example.librarianassistant.dto.OverdueItemResponse;
import com.example.librarianassistant.dto.PopularBookResponse;
import com.example.librarianassistant.model.Checkout;
import com.example.librarianassistant.repository.BookRepository;
import com.example.librarianassistant.repository.CheckoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private static final BigDecimal FINE_PER_DAY = new BigDecimal("0.25");

    private final CheckoutRepository checkoutRepository;
    private final BookRepository bookRepository;

    public CirculationReportResponse getCirculationStats(LocalDate from, LocalDate to) {
        long totalCheckouts = checkoutRepository.countByStatusAndCheckoutDateBetween(
                Checkout.CheckoutStatus.ACTIVE, from, to)
                + checkoutRepository.countByStatusAndCheckoutDateBetween(
                Checkout.CheckoutStatus.RETURNED, from, to)
                + checkoutRepository.countByStatusAndCheckoutDateBetween(
                Checkout.CheckoutStatus.OVERDUE, from, to);

        long activeCheckouts = checkoutRepository.countByStatus(Checkout.CheckoutStatus.ACTIVE);
        long overdueCheckouts = checkoutRepository.findOverdue(LocalDate.now()).size();
        long totalReturns = checkoutRepository.countByStatusAndReturnDateBetween(
                Checkout.CheckoutStatus.RETURNED, from, to);

        return CirculationReportResponse.builder()
                .totalCheckouts(totalCheckouts)
                .activeCheckouts(activeCheckouts)
                .overdueCheckouts(overdueCheckouts)
                .totalReturns(totalReturns)
                .periodFrom(from)
                .periodTo(to)
                .build();
    }

    public List<OverdueItemResponse> getOverdueItems() {
        LocalDate today = LocalDate.now();
        return checkoutRepository.findOverdue(today).stream()
                .map(c -> {
                    long daysOverdue = ChronoUnit.DAYS.between(c.getDueDate(), today);
                    BigDecimal accruedFine = FINE_PER_DAY.multiply(BigDecimal.valueOf(daysOverdue));
                    return OverdueItemResponse.builder()
                            .checkoutId(c.getId())
                            .userId(c.getUser().getId())
                            .userName(c.getUser().getName())
                            .bookId(c.getBook().getId())
                            .bookTitle(c.getBook().getTitle())
                            .dueDate(c.getDueDate())
                            .daysOverdue(daysOverdue)
                            .accruedFine(accruedFine)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<PopularBookResponse> getPopularBooks(int limit) {
        return bookRepository.findTopCheckoutedBooks(PageRequest.of(0, limit));
    }
}
