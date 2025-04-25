package com.example.tomatomall.Repository;

import com.example.tomatomall.po.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByOrderId(int orderId);
}
