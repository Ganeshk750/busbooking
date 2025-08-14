package com.hcltech.busbooking.service;

import com.hcltech.busbooking.model.Booking;
import com.hcltech.busbooking.model.Payment;
import com.hcltech.busbooking.repository.BookingRepository;
import com.hcltech.busbooking.repository.PaymentRepository;
import com.hcltech.busbooking.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Booking booking;
    private Payment payment;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setId(1L);
        booking.setUserName("TestUser");

        payment = new Payment();
        payment.setId(100L);
        payment.setAmount(2200.00);
        payment.setStatus("COMPLETED");
        payment.setBookingId(booking.getId());
    }

    @Test
    void whenPay_thenReturnSavedPayment() {
        Payment result = paymentService.pay(booking.getId(), 2200.00);
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(2200.00);
        assertThat(result.getStatus()).isEqualTo("COMPLETED");
    }

}

