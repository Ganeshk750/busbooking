package com.hcltech.busbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserBookingHistoryDto {

    @NotBlank(message = "User name is required")
    private String userName;

    @NotNull(message = "From date is required")
    @PastOrPresent(message = "Date must be past or in the present")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fromDate;

    @NotNull(message = "To date is required")
    @PastOrPresent(message = "Date must be past or in the present")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate toDate;

}
