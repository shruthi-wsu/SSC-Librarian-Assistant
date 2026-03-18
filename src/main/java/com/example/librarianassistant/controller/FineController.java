package com.example.librarianassistant.controller;

import com.example.librarianassistant.dto.FineResponse;
import com.example.librarianassistant.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FineResponse>> getUserFines(@PathVariable Long userId) {
        return ResponseEntity.ok(fineService.getUserFines(userId));
    }

    @GetMapping("/user/{userId}/unpaid")
    public ResponseEntity<List<FineResponse>> getUnpaidFines(@PathVariable Long userId) {
        return ResponseEntity.ok(fineService.getUnpaidFines(userId));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<FineResponse> payFine(@PathVariable Long id) {
        return ResponseEntity.ok(fineService.payFine(id));
    }

    @PostMapping("/{id}/waive")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<FineResponse> waiveFine(@PathVariable Long id) {
        return ResponseEntity.ok(fineService.waiveFine(id));
    }
}
