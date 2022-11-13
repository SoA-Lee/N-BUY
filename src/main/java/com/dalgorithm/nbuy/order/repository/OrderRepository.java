package com.dalgorithm.nbuy.order.repository;

import com.dalgorithm.nbuy.order.entity.Order;
import com.dalgorithm.nbuy.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByProductId(Long productId);

    long countByProductIdAndApplicantIdAndOrderStatusIn(Long productId, String applicantId, Collection<OrderStatus> orderStatus);
}
