package com.example.librarianassistant.dto;

import com.example.librarianassistant.model.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Book details returned by the API")
@Data
@Builder
public class BookResponse {

    @Schema(description = "Internal book ID", example = "1")
    private Long id;

    @Schema(description = "ISBN identifier", example = "978-0-618-57144-6")
    private String isbn;

    @Schema(description = "Book title", example = "The Fellowship of the Ring")
    private String title;

    @Schema(description = "Author full name", example = "J.R.R. Tolkien")
    private String author;

    @Schema(description = "Genre", example = "Fantasy")
    private String genre;

    @Schema(description = "Year of publication", example = "1954")
    private Integer publishYear;

    @Schema(description = "Total copies owned", example = "5")
    private Integer totalCopies;

    @Schema(description = "Copies available for checkout", example = "3")
    private Integer availableCopies;

    @Schema(description = "Shelf location", example = "A-12")
    private String location;

    @Schema(description = "Availability status", example = "AVAILABLE",
            allowableValues = {"AVAILABLE", "CHECKED_OUT", "UNAVAILABLE"})
    private Book.BookStatus status;
}
