package com.hcltech.busbooking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDto {

    @NotBlank(message = "User name is required")
    private String userName;

    @NotNull(message = "Bus ID is required")
    private Long busId;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date must be today or in the future")
    private LocalDate date;
}




