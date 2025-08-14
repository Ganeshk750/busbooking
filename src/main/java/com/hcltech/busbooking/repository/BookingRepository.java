package com.hcltech.busbooking.repository;

import com.hcltech.busbooking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserNameAndTravelDateBetween(String userName, LocalDate from, LocalDate to);
    List<Booking> findByTravelDateBetween(LocalDate from, LocalDate to);
    List<Booking> findByUserName(String userName);
    boolean existsByBusIdAndUserName(Long busId, String userName);
}
