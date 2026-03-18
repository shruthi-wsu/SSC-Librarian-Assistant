package com.example.librarianassistant.controller;

import com.example.librarianassistant.dto.CirculationReportResponse;
import com.example.librarianassistant.dto.OverdueItemResponse;
import com.example.librarianassistant.dto.PopularBookResponse;
import com.example.librarianassistant.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('LIBRARIAN')")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/circulation")
    public ResponseEntity<CirculationReportResponse> getCirculationStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(reportService.getCirculationStats(from, to));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<OverdueItemResponse>> getOverdueItems() {
        return ResponseEntity.ok(reportService.getOverdueItems());
    }

    @GetMapping("/popular-books")
    public ResponseEntity<List<PopularBookResponse>> getPopularBooks(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(reportService.getPopularBooks(limit));
    }
}
