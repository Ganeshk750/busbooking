package com.hcltech.busbooking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookingId;
    private double amount;
    private String status; //  COMPLETED
    private LocalDateTime paymentTime;

    public Payment(Long bookingId, double amount, String status, LocalDateTime paymentTime) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.status = status;
        this.paymentTime = paymentTime;
    }

    public Payment(long bookingId, double amount, String completed, Long id) {
    }
}
