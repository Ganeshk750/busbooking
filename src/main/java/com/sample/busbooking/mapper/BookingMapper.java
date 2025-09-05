package com.sample.busbooking.mapper;


import com.sample.busbooking.dto.BookingDto;
import com.sample.busbooking.model.Booking;
import com.sample.busbooking.model.Bus;

public class BookingMapper {

    private BookingMapper(){}

    public static Booking bookingMapper(BookingDto bookingDto, Bus bus){
        Booking booking = new Booking();
        booking.setUserName(bookingDto.getUserName());
        booking.setBus(bus);
        booking.setTravelDate(bookingDto.getDate());
        booking.setStatus("BOOKED");
        return booking;
    }

}
