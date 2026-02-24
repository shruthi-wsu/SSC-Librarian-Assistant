package com.example.librarianassistant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class BookRequest {

    @NotBlank
    private String isbn;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private String genre;

    private Integer publishYear;

    @NotNull
    @PositiveOrZero
    private Integer totalCopies;

    @NotNull
    @PositiveOrZero
    private Integer availableCopies;

    private String location;
}
