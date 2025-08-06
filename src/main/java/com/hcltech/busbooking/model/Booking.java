package com.hcltech.busbooking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    @ManyToOne
    private Bus bus;
    private LocalDate travelDate;
    private String status; // BOOKED, CANCELLED
    private Long paymentId;

    public Booking(Long paymentId, String status, LocalDate travelDate, Bus bus, String userName) {
        this.paymentId = paymentId;
        this.status = status;
        this.travelDate = travelDate;
        this.bus = bus;
        this.userName = userName;
    }
}
