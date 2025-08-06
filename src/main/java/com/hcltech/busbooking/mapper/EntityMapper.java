package com.hcltech.busbooking.mapper;


import com.hcltech.busbooking.dto.BookingDto;
import com.hcltech.busbooking.model.Booking;
import com.hcltech.busbooking.model.Bus;


public class EntityMapper {

    public static Booking bookingMapper(BookingDto bookingDto, Bus bus){
        Booking booking = new Booking();
        booking.setUserName(bookingDto.getUserName());
        booking.setBus(bus);
        booking.setTravelDate(bookingDto.getDate());
        booking.setStatus("BOOKED");
        return booking;
    }
}
