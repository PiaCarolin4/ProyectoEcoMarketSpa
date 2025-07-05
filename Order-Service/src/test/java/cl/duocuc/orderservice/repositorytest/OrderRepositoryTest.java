package cl.duocuc.orderservice.repositorytest;

import cl.duocuc.orderservice.model.Order;
import cl.duocuc.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testCargaInicialDeUsuarios() {
        List<Order> ordenes = orderRepository.findAll();
        assertEquals(15, ordenes.size(), "Deben cargarse 15 ordenes desde data.sql");
    }


}

