package com.sample.busbooking.repository;

import com.sample.busbooking.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findBySourceAndDestinationAndDepartureTimeBetween(
            String source, String destination,
            LocalDateTime start, LocalDateTime end);

    Bus findByRegistrationNo(String regNo);

}
