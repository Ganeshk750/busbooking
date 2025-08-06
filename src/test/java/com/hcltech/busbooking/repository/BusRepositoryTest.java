package com.hcltech.busbooking.repository;

import com.hcltech.busbooking.model.Bus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BusRepositoryTest {

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        busRepository.deleteAll();
    }

    @Test
    void whenFindByRegistrationNo_thenReturnBus() {
        Bus bus = new Bus("Goa", "Bangalore",
                LocalDateTime.now(),29, 2200.00,"KABC-55-2631");
        entityManager.persistAndFlush(bus);
        Bus found = busRepository.findByRegistrationNo(bus.getRegistrationNo());
        assertThat(found).isNotNull();
        assertThat(found.getRegistrationNo()).isEqualTo(bus.getRegistrationNo());
    }

    @Test
    void whenSaveBus_thenBusIsPersisted() {
        Bus bus = new Bus("Goa", "Bangalore",
                LocalDateTime.now(),29, 2200.00,"KABC-55-2631");

        Bus savedBus = busRepository.save(bus);

        assertThat(savedBus).isNotNull();
        assertThat(savedBus.getId()).isNotNull();
        assertThat(savedBus.getSource()).isEqualTo("Goa");
    }

    @Test
    void whenFindBySourceDestinationAndDepartureTimeBetween_thenReturnMatchingBuses() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusHours(1);
        LocalDateTime end = now.plusHours(2);

        Bus bus1 = new Bus("Goa", "Bangalore", now, 30,
                2000.00, "KA-01-1234");
        Bus bus2 = new Bus("Goa", "Bangalore", now.plusHours(1), 30,
                2100.00, "KA-01-5678");
        Bus bus3 = new Bus("Goa", "Bangalore", now.plusHours(3), 30,
                2200.00, "KA-01-9999");
        Bus bus4 = new Bus("Mumbai", "Bangalore", now, 30,
                2300.00, "KA-02-1111");

        entityManager.persist(bus1);
        entityManager.persist(bus2);
        entityManager.persist(bus3);
        entityManager.persist(bus4);
        entityManager.flush();

        List<Bus> foundBuses = busRepository.findBySourceAndDestinationAndDepartureTimeBetween(
                "Goa", "Bangalore", start, end);

        assertThat(foundBuses).hasSize(2);
        assertThat(foundBuses).extracting(Bus::getRegistrationNo)
                .containsExactlyInAnyOrder("KA-01-1234", "KA-01-5678");
    }


}
