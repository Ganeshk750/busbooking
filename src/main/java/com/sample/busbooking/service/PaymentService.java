package com.sample.busbooking.service;


import com.sample.busbooking.model.Payment;

public interface PaymentService {
    Payment pay(Long bookingId, double amount);
}
