package com.sample.busbooking.service.impl;


import com.sample.busbooking.dto.BusDto;
import com.sample.busbooking.model.Bus;
import com.sample.busbooking.repository.BusRepository;
import com.sample.busbooking.service.BusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

    static final Logger logger = LoggerFactory.getLogger(BusServiceImpl.class);

    private final BusRepository busRepository;
    private final ModelMapper modelMapper;

    @Override
    public String addBus(BusDto busDto) {
        logger.info("Processing Bus by RegistrationNo: {} ", busDto.getRegistrationNo());
        Bus dbBus = busRepository.findByRegistrationNo(busDto.getRegistrationNo());
        if(dbBus !=null){
            return "Bus Already exists!";
        }
        Bus bus = modelMapper.map(busDto, Bus.class);
        busRepository.save(bus);
        return "Bus added successfully!";

    }

    @Override
    public List<Bus> search(String src, String dest, LocalDate date) {
        logger.info("Processing Bus search based on input {} :: {} ", src, dest);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59);
        return busRepository.findBySourceAndDestinationAndDepartureTimeBetween(src, dest, start, end);
    }


}
