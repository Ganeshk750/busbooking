package com.sample.busbooking.service;

import com.sample.busbooking.dto.BusDto;
import com.sample.busbooking.model.Bus;

import java.time.LocalDate;
import java.util.List;

public interface BusService {
    String addBus(BusDto busDto);
    List<Bus> search(String src, String dest, LocalDate date);
}
