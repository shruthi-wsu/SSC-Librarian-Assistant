package com.example.librarianassistant.controller;

import com.example.librarianassistant.dto.HoldResponse;
import com.example.librarianassistant.service.HoldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/holds")
@RequiredArgsConstructor
public class HoldController {

    private final HoldService holdService;

    @PostMapping
    public ResponseEntity<HoldResponse> placeHold(@RequestParam Long userId,
                                                   @RequestParam Long bookId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(holdService.placeHold(userId, bookId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelHold(@PathVariable Long id, Authentication auth) {
        holdService.cancelHold(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HoldResponse>> getUserHolds(@PathVariable Long userId) {
        return ResponseEntity.ok(holdService.getUserHolds(userId));
    }

    @GetMapping("/book/{bookId}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<HoldResponse>> getHoldsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(holdService.getHoldsByBook(bookId));
    }
}
