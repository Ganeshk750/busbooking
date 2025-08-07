package com.hcltech.busbooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.repository.BusRepository;
import com.hcltech.busbooking.service.impl.BusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BusServiceTest {

    @Mock
    private BusRepository busRepository;

    @InjectMocks
    private BusServiceImpl busService;

    private Bus sampleBus;

    @BeforeEach
    void setUp() {
        sampleBus = new Bus("Goa", "Bangalore",
                LocalDateTime.of(2025, 7, 31, 10, 0),
                30, 2200.00, "KA-01-1234");
    }

    @Test
    void whenBusAlreadyAdded_thenReturnConfirmationMessage() {
        when(busRepository.findByRegistrationNo(sampleBus.getRegistrationNo())).thenReturn(sampleBus);
        String result = busService.addBus(sampleBus);
        assertEquals("Bus Already exists!", result);
        verify(busRepository, never()).save(any(Bus.class));
    }

    @Test
    void whenAddBus_thenReturnConfirmationMessage() {
        when(busRepository.save(any(Bus.class))).thenReturn(sampleBus);

        String result = busService.addBus(sampleBus);

        assertEquals("Bus added successfully!", result);
        verify(busRepository, times(1)).save(sampleBus);
    }


    @Test
    void whenSearch_thenReturnMatchingBuses() {
        LocalDate date = LocalDate.of(2025, 7, 31);
        List<Bus> expectedBuses = List.of(sampleBus);

        when(busRepository.findBySourceAndDestinationAndDepartureTimeBetween(
                eq("Goa"), eq("Bangalore"), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(expectedBuses);

        List<Bus> result = busService.search("Goa", "Bangalore", date);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRegistrationNo()).isEqualTo("KA-01-1234");

        verify(busRepository, times(1))
                .findBySourceAndDestinationAndDepartureTimeBetween(
                        eq("Goa"), eq("Bangalore"), any(LocalDateTime.class), any(LocalDateTime.class));
    }

}

