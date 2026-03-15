package com.example.librarianassistant.service;

import com.example.librarianassistant.dto.FineResponse;
import com.example.librarianassistant.exception.ResourceNotFoundException;
import com.example.librarianassistant.model.Fine;
import com.example.librarianassistant.repository.FineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;

    @Transactional(readOnly = true)
    public List<FineResponse> getUserFines(Long userId) {
        return fineRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FineResponse> getUnpaidFines(Long userId) {
        return fineRepository.findByUserIdAndStatus(userId, Fine.FineStatus.UNPAID).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FineResponse payFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found: " + fineId));
        fine.setStatus(Fine.FineStatus.PAID);
        fine.setPaidDate(LocalDate.now());
        return toResponse(fineRepository.save(fine));
    }

    @Transactional
    public FineResponse waiveFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found: " + fineId));
        fine.setStatus(Fine.FineStatus.WAIVED);
        fine.setPaidDate(LocalDate.now());
        return toResponse(fineRepository.save(fine));
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalUnpaid(Long userId) {
        return fineRepository.sumUnpaidFines(userId)
                .orElse(BigDecimal.ZERO);
    }

    private FineResponse toResponse(Fine f) {
        return FineResponse.builder()
                .id(f.getId())
                .userId(f.getUser().getId())
                .userName(f.getUser().getName())
                .checkoutId(f.getCheckout().getId())
                .amount(f.getAmount())
                .issuedDate(f.getIssuedDate())
                .paidDate(f.getPaidDate())
                .status(f.getStatus())
                .build();
    }
}
