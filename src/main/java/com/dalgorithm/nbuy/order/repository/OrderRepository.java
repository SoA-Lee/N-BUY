package com.dalgorithm.nbuy.order.repository;

import com.dalgorithm.nbuy.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
