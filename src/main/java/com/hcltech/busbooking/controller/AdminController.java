package com.hcltech.busbooking.controller;


import com.hcltech.busbooking.dto.BusDto;
import com.hcltech.busbooking.model.Booking;
import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.service.BookingService;
import com.hcltech.busbooking.service.BusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final BusService busService;

    private final BookingService bookingService;

    @PostMapping("/buses")
    public ResponseEntity<String> addBus(@RequestBody @Valid BusDto busDto){
        logger.info("Received bus request for new bus: {}", busDto.getRegistrationNo());
        return new ResponseEntity<>(busService.addBus(busDto), HttpStatus.CREATED);
    }

    @GetMapping("/booking/history")
    public ResponseEntity<List<Booking>> allHistory(@RequestParam("fromDate") String fromDate,
                                                    @RequestParam("toDate") String toDate){
        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);

        logger.info("Received booking history date range : {} :: {}", fromDate, toDate);
        return new ResponseEntity<>(bookingService.allHistory(from, to), HttpStatus.OK);
    }

    @PutMapping("/bookings/{id}/cancel")
    public ResponseEntity<Booking> cancelAnyBooking(@PathVariable Long id){
        logger.info("Received booking cancel request for bookingId: {}", id);
        return new ResponseEntity<>(bookingService.cancel(id), HttpStatus.OK);
    }
}
