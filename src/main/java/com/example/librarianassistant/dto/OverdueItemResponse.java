package com.example.librarianassistant.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class OverdueItemResponse {

    private Long checkoutId;
    private Long userId;
    private String userName;
    private Long bookId;
    private String bookTitle;
    private LocalDate dueDate;
    private long daysOverdue;
    private BigDecimal accruedFine;
}
