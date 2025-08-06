package com.hcltech.busbooking.repository;

import com.hcltech.busbooking.model.Booking;
import com.hcltech.busbooking.model.Bus;
import com.hcltech.busbooking.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private BusRepository busRepository;

    @Autowired
    private TestEntityManager entityManager;


    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
    }


    @Test
    void whenFindByUserName_thenReturnBookingList() {
        Bus bus = new Bus();
        busRepository.save(bus);
        Payment payment = new Payment();
        paymentRepository.save(payment);
        Booking booking = new Booking(payment.getId(), "COMPLETED", LocalDate.now(), bus, "TestUser");
        entityManager.persistAndFlush(booking);
        List<Booking> bookingList = bookingRepository.findByUserName(booking.getUserName());
        assertThat(bookingList.size()).isNotZero();
        assertThat(bookingList.getFirst().getUserName()).isEqualTo(booking.getUserName());
    }

    @Test
    void whenSaveBooking_thenBookingIsPersisted() {
        Bus bus = new Bus();
        busRepository.save(bus);
        Payment payment = new Payment();
        paymentRepository.save(payment);
        Booking booking = new Booking(payment.getId(), "COMPLETED", LocalDate.now(), bus, "TestUser");

        Booking savedBooking = bookingRepository.save(booking);

        assertThat(savedBooking).isNotNull();
        assertThat(savedBooking.getId()).isNotNull();
        assertThat(savedBooking.getUserName()).isEqualTo("TestUser");
    }

    @Test
    void whenFindByTravelDateBetween_thenReturnBookingsInDateRange() {
        Bus bus = new Bus();
        busRepository.save(bus);
        Payment payment = new Payment();
        paymentRepository.save(payment);

        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(1);
        LocalDate to = today.plusDays(2);

        Booking booking1 = new Booking(payment.getId(), "COMPLETED", today, bus, "User1");
        Booking booking2 = new Booking(payment.getId(), "COMPLETED",
                today.plusDays(1), bus, "User2");
        Booking booking3 = new Booking(payment.getId(), "COMPLETED",
                today.plusDays(3), bus, "User3");

        entityManager.persist(booking1);
        entityManager.persist(booking2);
        entityManager.persist(booking3);
        entityManager.flush();

        List<Booking> bookings = bookingRepository.findByTravelDateBetween(from, to);

        assertThat(bookings).hasSize(2);
        assertThat(bookings).extracting(Booking::getTravelDate)
                .containsExactlyInAnyOrder(today, today.plusDays(1));
    }


    @Test
    void whenFindByUserNameAndTravelDateBetween_thenReturnMatchingBookings() {
        Bus bus = new Bus();
        busRepository.save(bus);
        Payment payment = new Payment();
        paymentRepository.save(payment);

        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(1);
        LocalDate to = today.plusDays(2);

        Booking booking1 = new Booking(payment.getId(), "COMPLETED",
                today, bus, "TestUser");
        Booking booking2 = new Booking(payment.getId(), "COMPLETED",
                today.plusDays(1), bus, "TestUser");
        Booking booking3 = new Booking(payment.getId(), "COMPLETED",
                today.plusDays(3), bus, "TestUser");
        Booking booking4 = new Booking(payment.getId(), "COMPLETED",
                today, bus, "AnotherUser");

        entityManager.persist(booking1);
        entityManager.persist(booking2);
        entityManager.persist(booking3);
        entityManager.persist(booking4);
        entityManager.flush();

        List<Booking> bookings = bookingRepository
                .findByUserNameAndTravelDateBetween("TestUser", from, to);

        assertThat(bookings).hasSize(2);
        assertThat(bookings).extracting(Booking::getUserName).containsOnly("TestUser");
        assertThat(bookings).extracting(Booking::getTravelDate)
                .containsExactlyInAnyOrder(today, today.plusDays(1));

    }


}
