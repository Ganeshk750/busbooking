package com.hcltech.busbooking.service;


import com.hcltech.busbooking.model.Payment;

public interface PaymentService {
    Payment pay(Long bookingId, double amount);
}
