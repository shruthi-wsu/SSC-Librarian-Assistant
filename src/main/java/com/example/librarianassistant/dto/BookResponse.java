package com.example.librarianassistant.dto;

import com.example.librarianassistant.model.Book;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {

    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private Integer publishYear;
    private Integer totalCopies;
    private Integer availableCopies;
    private String location;
    private Book.BookStatus status;
}
