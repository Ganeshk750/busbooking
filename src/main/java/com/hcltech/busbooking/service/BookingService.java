package com.hcltech.busbooking.service;


import com.hcltech.busbooking.dto.BookingDto;
import com.hcltech.busbooking.dto.UserBookingHistoryDto;
import com.hcltech.busbooking.model.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    Booking book(BookingDto bookingDto);
    Booking cancel(Long bookingId);
    List<Booking> historyForUser(UserBookingHistoryDto userBookingHistoryDto);
    List<Booking> allHistory(LocalDate from, LocalDate to);

}
