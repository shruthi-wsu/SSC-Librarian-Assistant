package com.example.librarianassistant.repository;

import com.example.librarianassistant.model.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    List<Checkout> findByUserId(Long userId);

    List<Checkout> findByUserIdAndStatus(Long userId, Checkout.CheckoutStatus status);

    Optional<Checkout> findByUserIdAndBookIdAndStatus(Long userId, Long bookId, Checkout.CheckoutStatus status);

    @Query("SELECT c FROM Checkout c WHERE c.status = 'ACTIVE' AND c.dueDate < :today")
    List<Checkout> findOverdue(@Param("today") LocalDate today);

    long countByUserIdAndStatus(Long userId, Checkout.CheckoutStatus status);

    long countByStatus(Checkout.CheckoutStatus status);

    long countByStatusAndCheckoutDateBetween(Checkout.CheckoutStatus status, LocalDate from, LocalDate to);

    long countByStatusAndReturnDateBetween(Checkout.CheckoutStatus status, LocalDate from, LocalDate to);
}
