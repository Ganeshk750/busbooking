package com.hcltech.busbooking.service.impl;


import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.repository.BusRepository;
import com.hcltech.busbooking.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;

    @Override
    public String addBus(Bus bus) {
        Bus dbBus = busRepository.findByRegistrationNo(bus.getRegistrationNo());
        if(dbBus !=null){
            return "Bus Already exists!";
        }else{
            busRepository.save(bus);
            return "Bus added successfully!";
        }

    }

    @Override
    public List<Bus> search(String src, String dest, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59);
        return busRepository.findBySourceAndDestinationAndDepartureTimeBetween(src, dest, start, end);
    }
}
