package com.sample.busbooking.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.sample.busbooking.model.Bus;
import com.sample.busbooking.service.BusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


@ExtendWith(MockitoExtension.class)
public class BusControllerTest {

    @Mock
    private BusService busService;

    @InjectMocks
    private BusController busController;

    private Bus sampleBus;

    @BeforeEach
    void setUp() {
        sampleBus = new Bus("Goa", "Bangalore",
                LocalDateTime.of(2025, 7, 31, 10, 0),
                30, 2200.00, "KA-01-1234");
    }

    @Test
    void whenSearchBuses_thenReturnBusList() {
        List<Bus> buses = List.of(sampleBus);
        String source = "Goa";
        String destination = "Bangalore";
        String date = "2025-07-31";

        when(busService.search(source, destination, LocalDate.parse(date))).thenReturn(buses);

        ResponseEntity<List<Bus>> result = busController.search(source, destination, date);
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertThat(result.getBody().getFirst().getSource()).isEqualTo(source);
        assertThat(result.getBody().getFirst().getDestination()).isEqualTo(destination);
        assertThat(result.getBody().getFirst().getRegistrationNo()).isEqualTo(sampleBus.getRegistrationNo());
        assertThat(result.getBody()).hasSize(1);
    }
}

