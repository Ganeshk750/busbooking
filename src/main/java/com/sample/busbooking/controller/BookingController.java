package com.sample.busbooking.controller;

import com.sample.busbooking.dto.BookingDto;
import com.sample.busbooking.dto.UserBookingHistoryDto;
import com.sample.busbooking.model.Booking;
import com.sample.busbooking.service.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> book(@RequestBody @Valid BookingDto bookingDto){
        logger.info("Received booking request for user:: {}", bookingDto.getUserName());
        LocalDate localDate = LocalDate.parse(String.valueOf(bookingDto.getDate()));
        bookingDto.setDate(localDate);
        return new ResponseEntity<>(bookingService.book(bookingDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id){
        logger.info("Received booking cancel request for bookingId: {}", id);
        return new ResponseEntity<>(bookingService.cancel(id), HttpStatus.OK);
    }

    @PostMapping("/history")
    public ResponseEntity<List<Booking>> bookingHistoryForUser(@RequestBody @Valid UserBookingHistoryDto userBookingHistoryDto){
        logger.info("Received booking history for user ::: {}", userBookingHistoryDto.getUserName());
        LocalDate from = LocalDate.parse(String.valueOf(userBookingHistoryDto.getFromDate()));
        LocalDate to = LocalDate.parse(String.valueOf(userBookingHistoryDto.getToDate()));
        userBookingHistoryDto.setFromDate(from);
        userBookingHistoryDto.setToDate(to);
        return new ResponseEntity<>(bookingService.historyForUser(userBookingHistoryDto), HttpStatus.OK);
    }
}
