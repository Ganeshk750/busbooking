package com.hcltech.busbooking.service.impl;

import com.hcltech.busbooking.model.Payment;
import com.hcltech.busbooking.repository.PaymentRepository;
import com.hcltech.busbooking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment pay(Long bookingId, double amount) {
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setAmount(amount);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setStatus("COMPLETED");
        return paymentRepository.save(payment);
    }
}
