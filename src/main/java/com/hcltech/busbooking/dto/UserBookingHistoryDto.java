package com.hcltech.busbooking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserBookingHistoryDto {

    private String userName;

    private LocalDate fromDate;

    private LocalDate toDate;

}
