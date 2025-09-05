package com.sample.busbooking.service;


import com.sample.busbooking.dto.BookingDto;
import com.sample.busbooking.dto.UserBookingHistoryDto;
import com.sample.busbooking.model.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    Booking book(BookingDto bookingDto);
    Booking cancel(Long bookingId);
    List<Booking> historyForUser(UserBookingHistoryDto userBookingHistoryDto);
    List<Booking> allHistory(LocalDate from, LocalDate to);

}
