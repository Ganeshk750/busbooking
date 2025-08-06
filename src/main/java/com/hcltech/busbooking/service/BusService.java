package com.hcltech.busbooking.service;

import com.hcltech.busbooking.model.Bus;

import java.time.LocalDate;
import java.util.List;

public interface BusService {
    String addBus(Bus bus);
    List<Bus> search(String src, String dest, LocalDate date);
}
