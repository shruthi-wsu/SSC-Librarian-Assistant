package com.example.librarianassistant.controller;

import com.example.librarianassistant.dto.BookRequest;
import com.example.librarianassistant.dto.BookResponse;
import com.example.librarianassistant.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Books", description = "Book catalog – read access for all authenticated users; write access requires LIBRARIAN role")
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(summary = "List all books", description = "Returns every book in the catalog regardless of availability.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @Operation(summary = "Get a book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @Parameter(description = "Book ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(summary = "Search books by title, author, or ISBN",
               description = "Case-insensitive partial match against title, author, and ISBN fields.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search results (may be empty)",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchBooks(
            @Parameter(description = "Search term", example = "Tolkien") @RequestParam String query) {
        return ResponseEntity.ok(bookService.searchBooks(query));
    }

    @Operation(summary = "List available books", description = "Returns only books with at least one available copy.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Available books",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content)
    })
    @GetMapping("/available")
    public ResponseEntity<List<BookResponse>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }

    @Operation(summary = "Add a new book", description = "**Requires LIBRARIAN role.** Creates a new book record in the catalog.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or duplicate ISBN", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient role (LIBRARIAN required)", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(request));
    }

    @Operation(summary = "Update a book", description = "**Requires LIBRARIAN role.** Replaces all fields of the specified book.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient role (LIBRARIAN required)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookResponse> updateBook(
            @Parameter(description = "Book ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @Operation(summary = "Delete a book", description = "**Requires LIBRARIAN role.** Permanently removes the book from the catalog.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient role (LIBRARIAN required)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Book ID", example = "1") @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
