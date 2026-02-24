package com.example.librarianassistant.controller;

import com.example.librarianassistant.dto.CheckoutResponse;
import com.example.librarianassistant.service.CirculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/checkouts")
@RequiredArgsConstructor
public class CheckoutController {

    private final CirculationService circulationService;

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@RequestParam Long userId,
                                                     @RequestParam Long bookId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(circulationService.checkoutBook(userId, bookId));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<CheckoutResponse> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(circulationService.returnBook(id));
    }

    @PostMapping("/{id}/renew")
    public ResponseEntity<CheckoutResponse> renew(@PathVariable Long id) {
        return ResponseEntity.ok(circulationService.renewCheckout(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CheckoutResponse>> getUserCheckouts(@PathVariable Long userId) {
        return ResponseEntity.ok(circulationService.getUserCheckouts(userId));
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<CheckoutResponse>> getOverdueCheckouts() {
        return ResponseEntity.ok(circulationService.getOverdueCheckouts());
    }
}
