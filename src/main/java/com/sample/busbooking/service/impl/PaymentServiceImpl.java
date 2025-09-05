package com.sample.busbooking.service.impl;

import com.sample.busbooking.model.Payment;
import com.sample.busbooking.repository.PaymentRepository;
import com.sample.busbooking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    @Override
    public Payment pay(Long bookingId, double amount) {
        logger.info("Processing Payment for booking ID: {} ", bookingId);
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setAmount(amount);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setStatus("COMPLETED");
        paymentRepository.save(payment);
        logger.info("Payment is successfully completed For BookingId {} ", bookingId);
        return payment;
    }
}
