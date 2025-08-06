package com.hcltech.busbooking.controller;

import com.hcltech.busbooking.dto.BookingDto;
import com.hcltech.busbooking.dto.UserBookingHistoryDto;
import com.hcltech.busbooking.model.Booking;
import com.hcltech.busbooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> book(@RequestBody BookingDto bookingDto){
        LocalDate localDate = LocalDate.parse(String.valueOf(bookingDto.getDate()));
        bookingDto.setDate(localDate);
        return new ResponseEntity<>(bookingService.book(bookingDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id){
        return new ResponseEntity<>(bookingService.cancel(id), HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Booking>> bookingHistoryForUser(@RequestBody UserBookingHistoryDto userBookingHistoryDto){
        LocalDate from = LocalDate.parse(String.valueOf(userBookingHistoryDto.getFromDate()));
        LocalDate to = LocalDate.parse(String.valueOf(userBookingHistoryDto.getToDate()));
        userBookingHistoryDto.setFromDate(from);
        userBookingHistoryDto.setToDate(to);
        return new ResponseEntity<>(bookingService.historyForUser(userBookingHistoryDto), HttpStatus.OK);
    }
}
