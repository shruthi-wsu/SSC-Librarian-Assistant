package com.example.librarianassistant.dto;

import com.example.librarianassistant.model.Fine;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class FineResponse {

    private Long id;
    private Long userId;
    private String userName;
    private Long checkoutId;
    private BigDecimal amount;
    private LocalDate issuedDate;
    private LocalDate paidDate;
    private Fine.FineStatus status;
}
