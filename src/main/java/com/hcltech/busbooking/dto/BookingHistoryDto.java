package com.hcltech.busbooking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingHistoryDto {
    private LocalDate fromDate;
    private LocalDate toDate;
}
