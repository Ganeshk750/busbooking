package com.hcltech.busbooking.service.impl;

import com.hcltech.busbooking.dto.BookingDto;
import com.hcltech.busbooking.dto.UserBookingHistoryDto;
import com.hcltech.busbooking.exception.BookingCancelledException;
import com.hcltech.busbooking.exception.NoSeatExistsException;
import com.hcltech.busbooking.mapper.EntityMapper;
import com.hcltech.busbooking.model.Booking;
import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.model.Payment;
import com.hcltech.busbooking.repository.BookingRepository;
import com.hcltech.busbooking.repository.BusRepository;
import com.hcltech.busbooking.service.BookingService;
import com.hcltech.busbooking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BusRepository busRepository;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public Booking book(BookingDto bookingDto) {
        Bus bus = busRepository.findById(bookingDto.getBusId()).orElseThrow();
        if(bus.getAvailableSeats() <= 0) throw new NoSeatExistsException("No seats");
        bus.setAvailableSeats(bus.getAvailableSeats() - 1);
        busRepository.save(bus);
        Booking booking = EntityMapper.bookingMapper(bookingDto, bus);
        Booking savedBooking = bookingRepository.save(booking);
        Payment payment = paymentService.pay(savedBooking.getId(), bus.getFareCharge());
        savedBooking.setPaymentId(payment.getId());
        return bookingRepository.save(savedBooking);
    }

    @Override
    public Booking cancel(Long bookingId) {
        Booking booking  = bookingRepository.findById(bookingId).orElseThrow();
        if("CANCELLED".equals(booking.getStatus())) throw new BookingCancelledException("Already cancelled");
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
        Bus bus = booking.getBus();
        bus.setAvailableSeats(bus.getAvailableSeats() + 1);
        busRepository.save(bus);
        return booking;
    }

    @Override
    public List<Booking> historyForUser(UserBookingHistoryDto userBookingHistoryDto) {
        return bookingRepository.findByUserNameAndTravelDateBetween(userBookingHistoryDto.getUserName(),
                userBookingHistoryDto.getFromDate(),
                userBookingHistoryDto.getToDate());
    }

    @Override
    public List<Booking> allHistory(LocalDate from, LocalDate to) {
        return bookingRepository.findByTravelDateBetween(from, to);
    }

}
