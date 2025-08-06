package com.hcltech.busbooking.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hcltech.busbooking.dto.BookingDto;
import com.hcltech.busbooking.dto.UserBookingHistoryDto;
import com.hcltech.busbooking.model.Booking;
import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        Bus bus = new Bus("Goa", "Bangalore",
                LocalDateTime.of(2025, 7, 31, 10, 0),
                30, 2200.00, "KA-01-1234");
        bus.setId(1L);

        booking = new Booking(101L, "COMPLETED",
                LocalDate.of(2025, 7, 31), bus, "TestUser");
        booking.setId(1L);
    }

@Test
void whenBook_thenReturnCreatedBooking() throws Exception {
        BookingDto request = new BookingDto();
        request.setUserName("TestUser");
        request.setBusId(1L);
        request.setDate(LocalDate.parse("2025-07-31"));

        when(bookingService.book(any(BookingDto.class))).thenReturn(booking);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

    mockMvc.perform(post("/api/v1/bookings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.userName").value("TestUser"))
            .andExpect(jsonPath("$.status").value("COMPLETED"));

    verify(bookingService).book(any(BookingDto.class));


}


    @Test
    void whenCancelBooking_thenReturnUpdatedBooking() throws Exception {
        booking.setStatus("CANCELLED");
        when(bookingService.cancel(1L)).thenReturn(booking);

        mockMvc.perform(put("/api/v1/bookings/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        verify(bookingService).cancel(1L);
    }


    @Test
    void whenGetBookingHistoryForUser_thenReturnListOfBookings() throws Exception {


        UserBookingHistoryDto request = new UserBookingHistoryDto();
        request.setUserName("TestUser");
        request.setFromDate(LocalDate.of(2025, 7, 1));
        request.setToDate(LocalDate.of(2025, 7, 31));

        List<Booking> bookings = List.of(booking);

        when(bookingService.historyForUser(any(UserBookingHistoryDto.class)))
                .thenReturn(bookings);

        // Configure ObjectMapper to handle LocalDate
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(get("/api/v1/bookings/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].userName").value("TestUser"))
                .andExpect(jsonPath("$[0].status").value("COMPLETED"));

        verify(bookingService).historyForUser(any(UserBookingHistoryDto.class));



    }




}



