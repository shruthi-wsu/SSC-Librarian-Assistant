package com.example.librarianassistant.dto;

import com.example.librarianassistant.model.Hold;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class HoldResponse {

    private Long id;
    private Long userId;
    private String userName;
    private Long bookId;
    private String bookTitle;
    private LocalDate holdDate;
    private LocalDate expirationDate;
    private Integer queuePosition;
    private Hold.HoldStatus status;
}
