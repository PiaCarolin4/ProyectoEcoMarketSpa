package cl.duocuc.orderservice.servicetest;

import cl.duocuc.orderservice.model.Order;
import cl.duocuc.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void testAddOrder() {
        assertNull(orderService.findById("O016"));
        ArrayList<String> productos = new ArrayList<>();
        productos.add("P009");
        productos.add("P005");
        productos.add("P001");
        Order newOrder = new Order("O016","10", productos, 13000.00, "PENDING");
        orderService.addOrder(newOrder);
        assertNotNull(orderService.findById("O016"));
    }

    @Test
    void testFindAll() {
        List<Order> orders = orderService.findAll();
        assertFalse(orders.isEmpty());
    }

    @Test
    void testFindById() {
        Order order = orderService.findById("O010");
        assertNotNull(order);
        assertEquals("O010", order.getId());
    }

    @Test
    void testRemoveOrder() {
        boolean deleted = orderService.removeOrder("O002");
        assertTrue(deleted);
        Order order = orderService.findById("O002");
        assertNull(order);
    }

    @Test
    void testUpdateOrder() {
        Order order = orderService.findById("O009");
        assertNotNull(order);
        ArrayList<String> productos = new ArrayList<>();
        productos.add("P009");
        Order modifiedOrder = new Order("O009","9", productos, 2490.00, "DELIVERED");
        orderService.updateOrder("O009", modifiedOrder);
        Order newOrder = orderService.findById("O009");
        assertEquals("DELIVERED", newOrder.getStatus());

    }


}

