package com.example.librarianassistant.config;

import com.example.librarianassistant.model.Book;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.repository.BookRepository;
import com.example.librarianassistant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Bean
    CommandLineRunner seedData(PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.saveAll(List.of(
                        User.builder()
                                .name("Admin Librarian")
                                .email("admin@library.com")
                                .password(passwordEncoder.encode("admin123"))
                                .role(User.Role.LIBRARIAN)
                                .build(),
                        User.builder()
                                .name("Jane Patron")
                                .email("jane@library.com")
                                .password(passwordEncoder.encode("patron123"))
                                .role(User.Role.PATRON)
                                .build()
                ));
            }

            if (bookRepository.count() == 0) {
                bookRepository.saveAll(List.of(
                        Book.builder().isbn("978-0-06-112008-4").title("To Kill a Mockingbird")
                                .author("Harper Lee").genre("Fiction").publishYear(1960)
                                .totalCopies(3).availableCopies(3).location("A1").build(),
                        Book.builder().isbn("978-0-7432-7356-5").title("1984")
                                .author("George Orwell").genre("Dystopian").publishYear(1949)
                                .totalCopies(2).availableCopies(2).location("A2").build(),
                        Book.builder().isbn("978-0-7432-7357-2").title("The Great Gatsby")
                                .author("F. Scott Fitzgerald").genre("Fiction").publishYear(1925)
                                .totalCopies(2).availableCopies(2).location("B1").build(),
                        Book.builder().isbn("978-0-316-76948-0").title("The Catcher in the Rye")
                                .author("J.D. Salinger").genre("Fiction").publishYear(1951)
                                .totalCopies(1).availableCopies(1).location("B2").build(),
                        Book.builder().isbn("978-0-06-093546-9").title("To Kill a Mockingbird 2nd Ed")
                                .author("Harper Lee").genre("Fiction").publishYear(1988)
                                .totalCopies(1).availableCopies(1).location("A1").build()
                ));
            }
        };
    }
}
