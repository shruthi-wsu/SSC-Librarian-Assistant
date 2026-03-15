package com.example.librarianassistant.dto;

import com.example.librarianassistant.model.Checkout;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class CheckoutResponse {

    private Long id;
    private Long userId;
    private String userName;
    private Long bookId;
    private String bookTitle;
    private String bookIsbn;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Integer renewalCount;
    private BigDecimal fineAmount;
    private Checkout.CheckoutStatus status;
}
