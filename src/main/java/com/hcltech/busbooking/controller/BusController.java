package com.hcltech.busbooking.controller;

import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/buses")
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;

    @GetMapping("/search")
    public ResponseEntity<List<Bus>> search(@RequestParam("source") String source,
                                            @RequestParam("destination") String destination,
                                            @RequestParam("date") String date){
        LocalDate localDate = LocalDate.parse(date);
        return new ResponseEntity<>(busService.search(source, destination, localDate), HttpStatus.OK);
    }
}
