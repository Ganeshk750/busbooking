package com.hcltech.busbooking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private int availableSeats;
    private double fareCharge;
    private String registrationNo;


    public Bus(String source, String destination, LocalDateTime departureTime, int availableSeats, double fareCharge, String registrationNo) {
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
        this.fareCharge = fareCharge;
        this.registrationNo = registrationNo;
    }
}
