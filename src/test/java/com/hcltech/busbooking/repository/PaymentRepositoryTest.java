package com.hcltech.busbooking.repository;


import com.hcltech.busbooking.model.Booking;
import com.hcltech.busbooking.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TestEntityManager entityManager;


    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
    }

    @Test
    void whenFindById_thenReturnPayment() {
        Booking booking = new Booking();
        Payment payment = new Payment(booking.getId(), 2500.00, "COMPLETE",
                LocalDateTime.now());
        entityManager.persistAndFlush(payment);
        Optional<Payment> found = paymentRepository.findById(payment.getId());
        assertThat(found.get().getId()).isNotNull();
        assertThat(found.get().getId()).isEqualTo(payment.getId());
    }

    @Test
    void whenSavePayment_thenPaymentIsPersisted() {
        Booking booking = new Booking();
        Payment payment = new Payment(booking.getId(), 2500.00, "COMPLETE",
                LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        assertThat(savedPayment).isNotNull();
        assertThat(savedPayment.getId()).isNotNull();
        assertThat(savedPayment.getStatus()).isEqualTo("COMPLETE");
    }

}
