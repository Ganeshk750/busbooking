package com.hcltech.busbooking.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.service.BusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(BusController.class)
public class BusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BusService busService;

    private Bus sampleBus;

    @BeforeEach
    void setUp() {
        sampleBus = new Bus("Goa", "Bangalore",
                LocalDateTime.of(2025, 7, 31, 10, 0),
                30, 2200.00, "KA-01-1234");
    }

    @Test
    void whenSearchBuses_thenReturnBusList() throws Exception {
        List<Bus> buses = List.of(sampleBus);
        String source = "Goa";
        String destination = "Bangalore";
        String date = "2025-07-31";

        when(busService.search(source, destination, LocalDate.parse(date))).thenReturn(buses);

        mockMvc.perform(get("/api/v1/buses/search")
                        .param("source", source)
                        .param("destination", destination)
                        .param("date", date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].source").value("Goa"))
                .andExpect(jsonPath("$[0].destination").value("Bangalore"))
                .andExpect(jsonPath("$[0].registrationNo").value("KA-01-1234"));

        verify(busService, times(1))
                .search(source, destination, LocalDate.parse(date));
    }
}

