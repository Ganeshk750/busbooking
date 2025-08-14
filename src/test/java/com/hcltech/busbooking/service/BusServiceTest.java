package com.hcltech.busbooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.hcltech.busbooking.dto.BusDto;
import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.repository.BusRepository;
import com.hcltech.busbooking.service.impl.BusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class BusServiceTest {

    @Mock
    private BusRepository busRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BusServiceImpl busService;

    private Bus sampleBus;
    private BusDto sampleBusDto;

    @BeforeEach
    void setUp() {
        sampleBus = new Bus("Goa", "Bangalore",
                LocalDateTime.of(2025, 7, 31, 10, 0),
                30, 2200.00, "KA-01-1234");

        sampleBusDto = new BusDto();
        sampleBusDto.setSource(sampleBus.getSource());
        sampleBusDto.setDestination(sampleBus.getDestination());
        sampleBusDto.setDepartureTime(sampleBus.getDepartureTime());
        sampleBusDto.setAvailableSeats(sampleBus.getAvailableSeats());
        sampleBusDto.setFareCharge(sampleBus.getFareCharge());
        sampleBusDto.setRegistrationNo(sampleBus.getRegistrationNo());
    }

    @Test
    void whenBusAlreadyAdded_thenReturnConfirmationMessage() {
        when(busRepository.findByRegistrationNo(sampleBusDto.getRegistrationNo())).thenReturn(sampleBus);
        String result = busService.addBus(sampleBusDto);
        assertEquals("Bus Already exists!", result);
        verify(busRepository, times(1)).findByRegistrationNo(sampleBusDto.getRegistrationNo());
        verify(busRepository, never()).save(any(Bus.class));
        verify(modelMapper, never()).map(any(BusDto.class), eq(Bus.class));
    }

    @Test
    void whenAddBus_thenReturnConfirmationMessage() {
        when(modelMapper.map(any(BusDto.class), eq(Bus.class))).thenReturn(sampleBus);
        when(busRepository.save(any(Bus.class))).thenReturn(sampleBus);
        String result = busService.addBus(sampleBusDto);
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