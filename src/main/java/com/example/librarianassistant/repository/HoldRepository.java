package com.example.librarianassistant.repository;

import com.example.librarianassistant.model.Hold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoldRepository extends JpaRepository<Hold, Long> {

    List<Hold> findByUserIdOrderByHoldDateAsc(Long userId);

    List<Hold> findByBookIdAndStatusOrderByHoldDateAsc(Long bookId, Hold.HoldStatus status);

    Optional<Hold> findByUserIdAndBookIdAndStatus(Long userId, Long bookId, Hold.HoldStatus status);

    long countByBookIdAndStatus(Long bookId, Hold.HoldStatus status);

    Optional<Hold> findTopByBookIdAndStatusOrderByHoldDateAsc(Long bookId, Hold.HoldStatus status);
}
