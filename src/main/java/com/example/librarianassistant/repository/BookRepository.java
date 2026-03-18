package com.example.librarianassistant.repository;

import com.example.librarianassistant.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Book> search(@Param("query") String query);

    List<Book> findByGenreIgnoreCase(String genre);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByAvailableCopiesGreaterThan(int copies);

    @Query("SELECT new com.example.librarianassistant.dto.PopularBookResponse(" +
           "b.id, b.title, b.author, COUNT(c.id)) " +
           "FROM Book b LEFT JOIN b.checkouts c " +
           "GROUP BY b.id, b.title, b.author " +
           "ORDER BY COUNT(c.id) DESC")
    List<com.example.librarianassistant.dto.PopularBookResponse> findTopCheckoutedBooks(org.springframework.data.domain.Pageable pageable);
}
