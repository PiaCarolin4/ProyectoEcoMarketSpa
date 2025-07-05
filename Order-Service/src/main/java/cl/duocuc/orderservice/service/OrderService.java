package cl.duocuc.orderservice.service;

import cl.duocuc.orderservice.model.Order;

import cl.duocuc.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository OrderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.OrderRepository = orderRepository;
    }

    public List<Order> findAll() {
        return OrderRepository.findAll();
    }

    public Order findById(String id) {
        return OrderRepository.findById(id).orElse(null);
    }

    public boolean addOrder(Order order) {
        if (OrderRepository.existsById(order.getId())) {
            return false;
        }
        OrderRepository.save(order);
        return true;
    }

    public boolean updateOrder(String id, Order order) {
        if (OrderRepository.existsById(id)) {
            OrderRepository.save(order);
            return true;
        }
        return false;
    }

    public boolean removeOrder(String id) {
        if (OrderRepository.existsById(id)) {
            OrderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
