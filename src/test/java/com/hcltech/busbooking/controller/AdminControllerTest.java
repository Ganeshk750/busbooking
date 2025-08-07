package com.hcltech.busbooking.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.hcltech.busbooking.model.Booking;
import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.service.BookingService;
import com.hcltech.busbooking.service.BusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BusService busService;

    @MockitoBean
    private BookingService bookingService;

    private Bus bus;
    private Booking booking;

    @BeforeEach
    void setUp() {
        bus = new Bus("Goa", "Bangalore",
                LocalDateTime.of(2025, 7, 31, 10, 0),
                30, 2200.00, "KA-01-1234");
        bus.setId(1L);

        booking = new Booking(101L, "COMPLETED",
                LocalDate.of(2025, 7, 31), bus, "TestUser");
        booking.setId(1L);
    }

    @Test
    void whenAddBus_thenReturnSuccessMessage() throws Exception {
        when(busService.addBus(any(Bus.class))).thenReturn("Bus added successfully");

        mockMvc.perform(post("/api/v1/admin/buses")
                        .contentType("application/json")
                        .content("""
                    {
                        "source": "Goa",
                        "destination": "Bangalore",
                        "departureTime": "2025-07-31T10:00:00",
                        "seats": 30,
                        "fare": 2200.00,
                        "registrationNo": "KA-01-1234"
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(content().string("Bus added successfully"));

        verify(busService).addBus(any(Bus.class));
    }

    @Test
    void whenGetAllBookingHistory_thenReturnListOfBookings() throws Exception {
        List<Booking> bookings = List.of(booking);
        when(bookingService.allHistory(LocalDate.parse("2025-07-01"),
                LocalDate.parse("2025-07-31")))
                .thenReturn(bookings);

        mockMvc.perform(get("/api/v1/admin/booking/history")
                        .param("fromDate", "2025-07-01")
                        .param("toDate", "2025-07-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].userName").value("TestUser"));

        verify(bookingService).allHistory(LocalDate.parse("2025-07-01"),
                LocalDate.parse("2025-07-31"));
    }



    @Test
    void whenCancelAnyBooking_thenReturnCancelledBooking() throws Exception {
        booking.setStatus("CANCELLED");
        when(bookingService.cancel(1L)).thenReturn(booking);

        mockMvc.perform(put("/api/v1/admin/bookings/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        verify(bookingService).cancel(1L);
    }
}

