package com.example.librarianassistant.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "holds")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    @Builder.Default
    private LocalDate holdDate = LocalDate.now();

    private LocalDate expirationDate;

    private Integer queuePosition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private HoldStatus status = HoldStatus.PENDING;

    public enum HoldStatus {
        PENDING, NOTIFIED, FULFILLED, CANCELLED, EXPIRED
    }
}
