package cl.duocuc.orderservice.repository;

import cl.duocuc.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}