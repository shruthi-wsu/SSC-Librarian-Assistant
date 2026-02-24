package com.example.librarianassistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Schema(description = "Payload for creating or updating a book")
@Data
public class BookRequest {

    @Schema(description = "ISBN-13 or ISBN-10 identifier", example = "978-0-618-57144-6")
    @NotBlank
    private String isbn;

    @Schema(description = "Book title", example = "The Fellowship of the Ring")
    @NotBlank
    private String title;

    @Schema(description = "Author full name", example = "J.R.R. Tolkien")
    @NotBlank
    private String author;

    @Schema(description = "Genre or subject category", example = "Fantasy")
    private String genre;

    @Schema(description = "Year of publication", example = "1954")
    private Integer publishYear;

    @Schema(description = "Total number of physical copies owned", example = "5")
    @NotNull
    @PositiveOrZero
    private Integer totalCopies;

    @Schema(description = "Copies currently available for checkout", example = "3")
    @NotNull
    @PositiveOrZero
    private Integer availableCopies;

    @Schema(description = "Shelf or aisle location in the library", example = "A-12")
    private String location;
}
