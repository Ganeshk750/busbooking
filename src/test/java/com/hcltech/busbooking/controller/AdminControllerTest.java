package com.hcltech.busbooking.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.hcltech.busbooking.dto.BusDto;
import com.hcltech.busbooking.model.Booking;
import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.service.BookingService;
import com.hcltech.busbooking.service.BusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private BusService busService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BookingService bookingService;
    @InjectMocks
    private AdminController adminController;

    private Bus bus;
    private Booking booking;
    private BusDto sampleBusDto;


    @BeforeEach
    void setUp() {
        bus = new Bus("Goa", "Bangalore",
                LocalDateTime.of(2025, 7, 31, 10, 0),
                30, 2200.00, "KA-01-1234");
        bus.setId(1L);

        booking = new Booking(101L, "COMPLETED",
                LocalDate.of(2025, 7, 31), bus, "TestUser");
        booking.setId(1L);
        sampleBusDto = new BusDto();
        sampleBusDto.setSource(bus.getSource());
        sampleBusDto.setDestination(bus.getDestination());
        sampleBusDto.setDepartureTime(bus.getDepartureTime());
        sampleBusDto.setAvailableSeats(bus.getAvailableSeats());
        sampleBusDto.setFareCharge(bus.getFareCharge());
        sampleBusDto.setRegistrationNo(bus.getRegistrationNo());
    }

    @Test
    void whenAddBus_thenReturnSuccessMessage() {
        when(busService.addBus(any(BusDto.class))).thenReturn("Bus added successfully");
        ResponseEntity<String> result = adminController.addBus(sampleBusDto);
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertThat(result.getBody()).isEqualTo("Bus added successfully");
        verify(busService, times(1)).addBus(sampleBusDto);
    }


    @Test
    void whenGetAllBookingHistory_thenReturnListOfBookings() {
        List<Booking> bookings = List.of(booking);
        when(bookingService.allHistory(LocalDate.parse("2025-07-01"),
                LocalDate.parse("2025-07-31")))
                .thenReturn(bookings);
        ResponseEntity<List<Booking>> result = adminController.allHistory("2025-07-01", "2025-07-31");
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertThat(result.getBody().getFirst().getUserName()).isEqualTo("TestUser");
        assertThat(result.getBody()).hasSize(1);
    }



    @Test
    void whenCancelAnyBooking_thenReturnCancelledBooking() {
        booking.setStatus("CANCELLED");
        when(bookingService.cancel(1L)).thenReturn(booking);

        ResponseEntity<Booking> result = adminController.cancelAnyBooking(1L);
        assertNotNull(result);
        assertThat(result.getBody().getStatus()).isEqualTo("CANCELLED");

    }

}



