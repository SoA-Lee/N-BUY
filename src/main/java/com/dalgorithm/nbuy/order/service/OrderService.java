package com.dalgorithm.nbuy.order.service;

import com.dalgorithm.nbuy.order.entity.OrderInput;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    /**
     * 주문
     */
    @Transactional
    void reqPurchase(OrderInput parameter);
}
