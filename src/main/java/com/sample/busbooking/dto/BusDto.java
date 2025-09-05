package com.sample.busbooking.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusDto {
    @NotBlank(message = "Source name is required")
    private String source;

    @NotBlank(message = "Destination name is required")
    private String destination;

    @NotNull(message = "DepartureDate&Time is required")
    @FutureOrPresent(message = "Date must be future or in the present")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime departureTime;

    @NotNull(message = "AvailableSeats count is required")
    @Min(value = 1, message = "AvailableSeats must be at least 1")
    private Integer availableSeats;

    @NotNull(message = "FareCharge is required")
    @DecimalMin(value = "500.0", message = "FareCharge must be positive or zero")
    private Double fareCharge;

    @NotBlank(message = "RegistrationNo is required")
    private String registrationNo;
}
