package com.hcltech.busbooking.service.impl;

import com.hcltech.busbooking.dto.BookingDto;
import com.hcltech.busbooking.dto.UserBookingHistoryDto;
import com.hcltech.busbooking.exception.BookingCancelledException;
import com.hcltech.busbooking.exception.DuplicateBookingException;
import com.hcltech.busbooking.exception.NoSeatExistsException;
import com.hcltech.busbooking.mapper.BookingMapper;
import com.hcltech.busbooking.model.Booking;
import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.model.Payment;
import com.hcltech.busbooking.repository.BookingRepository;
import com.hcltech.busbooking.repository.BusRepository;
import com.hcltech.busbooking.service.BookingService;
import com.hcltech.busbooking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;
    private final BusRepository busRepository;
    private final PaymentService paymentService;


    @Override
    @Transactional
    public Booking book(BookingDto bookingDto) {
        logger.info("Processing booking for bus ID: {} and user: {}", bookingDto.getBusId(), bookingDto.getUserName());
        if (bookingRepository.existsByBusIdAndUserName(bookingDto.getBusId(), bookingDto.getUserName())) {
            throw new DuplicateBookingException("User has already booked this bus");
        }
        Bus bus = busRepository.findById(bookingDto.getBusId()).orElseThrow();
        Optional.of(bus)
                .filter(b -> b.getAvailableSeats() > 0)
                .orElseThrow(() -> new NoSeatExistsException("No seats available"));
        bus.setAvailableSeats(bus.getAvailableSeats() - 1);
        busRepository.save(bus);
        Booking booking = BookingMapper.bookingMapper(bookingDto, bus);
        Booking savedBooking = bookingRepository.save(booking);
        Payment payment = paymentService.pay(savedBooking.getId(), bus.getFareCharge());
        savedBooking.setPaymentId(payment.getId());
        logger.debug("Booking completed for user: {}", bookingDto.getUserName());
        return bookingRepository.save(savedBooking);
    }


    @Override
    public Booking cancel(Long bookingId) {
        logger.info("Processing booking by bookingId ID: {} ", bookingId);
        Booking booking  = bookingRepository.findById(bookingId).orElseThrow();
        Optional.of(booking)
                .filter(b -> !"CANCELLED".equals(b.getStatus()))
                .orElseThrow(() -> new BookingCancelledException("Booking is already cancelled"));
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
        Bus bus = booking.getBus();
        bus.setAvailableSeats(bus.getAvailableSeats() + 1);
        busRepository.save(bus);
        logger.debug("Booking cancelled for bookingId : {}", bookingId );
        return booking;
    }

    @Override
    public List<Booking> historyForUser(UserBookingHistoryDto userBookingHistoryDto) {
        logger.info("Inside user booking history : {}", userBookingHistoryDto.getUserName() );
        return bookingRepository.findByUserNameAndTravelDateBetween(userBookingHistoryDto.getUserName(),
                userBookingHistoryDto.getFromDate(),
                userBookingHistoryDto.getToDate());
    }

    @Override
    public List<Booking> allHistory(LocalDate from, LocalDate to) {
        logger.info("Inside booking history using date range : {} :: {}", from, to);
        return bookingRepository.findByTravelDateBetween(from, to);
    }

}
