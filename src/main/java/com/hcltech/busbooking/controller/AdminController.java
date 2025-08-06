package com.hcltech.busbooking.controller;


import com.hcltech.busbooking.model.Booking;
import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.service.BookingService;
import com.hcltech.busbooking.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BusService busService;

    private final BookingService bookingService;

    @PostMapping("/buses")
    public ResponseEntity<String> addBus(@RequestBody Bus bus){
        return new ResponseEntity<>(busService.addBus(bus), HttpStatus.CREATED);
    }

    @GetMapping("/booking/history")
    public ResponseEntity<List<Booking>> allHistory(@RequestParam("fromDate") String fromDate,
                                                    @RequestParam("toDate") String toDate){
        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);
        return new ResponseEntity<>(bookingService.allHistory(from, to), HttpStatus.OK);
    }

    @PutMapping("/bookings/{id}/cancel")
    public ResponseEntity<Booking> cancelAnyBooking(@PathVariable Long id){
        return new ResponseEntity<>(bookingService.cancel(id), HttpStatus.OK);
    }
}
