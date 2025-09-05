package com.sample.busbooking.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.sample.busbooking.dto.BookingDto;
import com.sample.busbooking.dto.ErrorMessage;
import com.sample.busbooking.dto.UserBookingHistoryDto;
import com.hcltech.busbooking.exception.*;
import com.sample.busbooking.exception.BookingCancelledException;
import com.sample.busbooking.exception.ControllerExceptionHandler;
import com.sample.busbooking.exception.DuplicateBookingException;
import com.sample.busbooking.exception.NoSeatExistsException;
import com.sample.busbooking.model.Booking;
import com.sample.busbooking.model.Bus;
import com.sample.busbooking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private Booking booking;
    @InjectMocks
    private BookingController bookingController;

    private ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();

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
void whenBook_thenReturnCreatedBooking() {
        BookingDto request = new BookingDto();
        request.setUserName("TestUser");
        request.setBusId(1L);
        request.setDate(LocalDate.parse("2025-07-31"));
        when(bookingService.book(any(BookingDto.class))).thenReturn(booking);
        ResponseEntity<Booking> result = bookingController.book(request);
        assertNotNull(result);

}

    @Test
    void whenCancelBooking_thenReturnUpdatedBooking() {
        booking.setStatus("CANCELLED");
        when(bookingService.cancel(1L)).thenReturn(booking);
       ResponseEntity<Booking> result = bookingController.cancelBooking(1L);
        assertNotNull(result);
        assertThat(result.getBody().getStatus()).isEqualTo("CANCELLED");

    }


    @Test
    void whenGetBookingHistoryForUser_thenReturnListOfBookings() {

        UserBookingHistoryDto request = new UserBookingHistoryDto();
        request.setUserName("TestUser");
        request.setFromDate(LocalDate.of(2025, 7, 1));
        request.setToDate(LocalDate.of(2025, 7, 31));

        List<Booking> bookings = List.of(booking);

        when(bookingService.historyForUser(any(UserBookingHistoryDto.class)))
                .thenReturn(bookings);
        ResponseEntity<List<Booking>> result = bookingController.bookingHistoryForUser(request);
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertThat(result.getBody().getFirst().getUserName()).isEqualTo("TestUser");
        assertThat(result.getBody()).hasSize(1);

    }

    @Test
    public void testHandleNoSeatExistsException() {
        NoSeatExistsException ex = new NoSeatExistsException("No seats");
        WebRequest webRequest = mock(WebRequest.class);
        ResponseEntity<ErrorMessage> response = controllerExceptionHandler.noSeatExistsException(ex,webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No seats", response.getBody().getMessage());
    }

    @Test
    public void testHandleBookingCancelledException() {
        BookingCancelledException ex = new BookingCancelledException("Booking is already cancelled");
        WebRequest webRequest = mock(WebRequest.class);
        ResponseEntity<ErrorMessage> response = controllerExceptionHandler.bookingCancelledException(ex,webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Booking is already cancelled", response.getBody().getMessage());
    }

    @Test
    public void testHandleDuplicateBookingException() {
        DuplicateBookingException ex = new DuplicateBookingException("User has already booked this bus");
        WebRequest webRequest = mock(WebRequest.class);
        ResponseEntity<ErrorMessage> response = controllerExceptionHandler.duplicateBookingException(ex,webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User has already booked this bus", response.getBody().getMessage());
    }


}



