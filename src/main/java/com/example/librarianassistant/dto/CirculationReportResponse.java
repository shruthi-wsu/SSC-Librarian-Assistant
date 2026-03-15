package com.example.librarianassistant.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CirculationReportResponse {

    private long totalCheckouts;
    private long activeCheckouts;
    private long overdueCheckouts;
    private long totalReturns;
    private LocalDate periodFrom;
    private LocalDate periodTo;
}
