package com.example.librarianassistant.service;

import com.example.librarianassistant.dto.BookRequest;
import com.example.librarianassistant.dto.BookResponse;
import com.example.librarianassistant.exception.BusinessException;
import com.example.librarianassistant.exception.ResourceNotFoundException;
import com.example.librarianassistant.model.Book;
import com.example.librarianassistant.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BookResponse getBookById(Long id) {
        return toResponse(findById(id));
    }

    public List<BookResponse> searchBooks(String query) {
        return bookRepository.search(query).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookResponse> getAvailableBooks() {
        return bookRepository.findByAvailableCopiesGreaterThan(0).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BookResponse createBook(BookRequest request) {
        if (bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
            log.warn("Attempt to create book with duplicate ISBN: {}", request.getIsbn());
            throw new BusinessException("ISBN already exists: " + request.getIsbn());
        }
        Book book = Book.builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .author(request.getAuthor())
                .genre(request.getGenre())
                .publishYear(request.getPublishYear())
                .totalCopies(request.getTotalCopies())
                .availableCopies(request.getAvailableCopies())
                .location(request.getLocation())
                .build();
        BookResponse saved = toResponse(bookRepository.save(book));
        log.info("Book created: id={}, isbn={}, title={}", saved.getId(), saved.getIsbn(), saved.getTitle());
        return saved;
    }

    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = findById(id);
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setPublishYear(request.getPublishYear());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getAvailableCopies());
        book.setLocation(request.getLocation());
        BookResponse updated = toResponse(bookRepository.save(book));
        log.info("Book updated: id={}, title={}", id, updated.getTitle());
        return updated;
    }

    public void deleteBook(Long id) {
        bookRepository.delete(findById(id));
        log.info("Book deleted: id={}", id);
    }

    private Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
    }

    private BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .publishYear(book.getPublishYear())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .location(book.getLocation())
                .status(book.getStatus())
                .build();
    }
}
