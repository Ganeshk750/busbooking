package com.hcltech.busbooking.dto;

import lombok.*;

import java.time.LocalDate;


@Data
public class BookingDto {

    private String userName;

    private Long busId;

    private LocalDate date;

}
