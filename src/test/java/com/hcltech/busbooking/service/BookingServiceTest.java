package com.hcltech.busbooking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hcltech.busbooking.dto.BookingDto;
import com.hcltech.busbooking.dto.UserBookingHistoryDto;
import com.hcltech.busbooking.model.Booking;
import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.model.Payment;
import com.hcltech.busbooking.repository.BookingRepository;
import com.hcltech.busbooking.repository.BusRepository;
import com.hcltech.busbooking.repository.PaymentRepository;
import com.hcltech.busbooking.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BusRepository busRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Bus bus;
    private Booking booking;
    private Payment payment;


    @BeforeEach
    void setUp() {
        bus = new Bus("Goa", "Bangalore",
                LocalDateTime.of(2025, 7, 31, 10, 0),
                30, 2200.00, "KA-01-1234");
        bus.setId(1L);

        booking = new Booking(101L, "COMPLETED", LocalDate.of(2025, 7, 31), bus, "TestUser");
        booking.setId(1L);

        payment = new Payment(booking.getId(), 2200.00, "COMPLETED", LocalDateTime.now());
    }


    @Test
    void whenBook_thenReturnBooking() {
        bus.setAvailableSeats(5);
        when(busRepository.findById(bus.getId())).thenReturn(Optional.of(bus));
        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> {
                    Booking b = invocation.getArgument(0);
                    b.setId(1L);
                    return b;
                });
        when(paymentService.pay(booking.getId(), bus.getFareCharge())).thenReturn(payment);

        BookingDto request = new BookingDto();
        request.setUserName("TestUser");
        request.setBusId(bus.getId());
        request.setDate(booking.getTravelDate());

        Booking result = bookingService.book(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }


    @Test
    void whenCancel_thenReturnCancelledBooking() {
        booking.setStatus("BOOKED");
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.cancel(booking.getId());

        assertThat(result.getStatus()).isEqualTo("CANCELLED");
        verify(bookingRepository).findById(booking.getId());
        verify(bookingRepository).save(any(Booking.class));
    }


    @Test
    void whenHistoryForUser_thenReturnUserBookings() {
        LocalDate from = LocalDate.of(2025, 7, 1);
        LocalDate to = LocalDate.of(2025, 7, 31);
        List<Booking> expected = List.of(booking);

        UserBookingHistoryDto request = new UserBookingHistoryDto();
        request.setUserName("TestUser");
        request.setFromDate(from);
        request.setToDate(to);

        when(bookingRepository.findByUserNameAndTravelDateBetween("TestUser", from, to))
                .thenReturn(expected);

        List<Booking> result = bookingService.historyForUser(request);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("TestUser");
        verify(bookingRepository).findByUserNameAndTravelDateBetween("TestUser", from, to);
    }


    @Test
    void whenAllHistory_thenReturnAllBookingsInRange() {
        LocalDate from = LocalDate.of(2025, 7, 1);
        LocalDate to = LocalDate.of(2025, 7, 31);
        List<Booking> expected = List.of(booking);

        when(bookingRepository.findByTravelDateBetween(from, to)).thenReturn(expected);

        UserBookingHistoryDto request = new UserBookingHistoryDto();
        request.setUserName("TestUser");
        request.setFromDate(from);
        request.setToDate(to);
        List<Booking> result = bookingService.allHistory(from, to);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTravelDate()).isEqualTo(booking.getTravelDate());
        verify(bookingRepository).findByTravelDateBetween(from, to);
    }
}

