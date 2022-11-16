package com.dalgorithm.nbuy.order.service;

import com.dalgorithm.nbuy.order.dto.OrderDto;
import com.dalgorithm.nbuy.order.entity.OrderInput;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {
    /**
     * 같이구매 참여 신청
     */
    @Transactional
    void reqPurchase(OrderInput parameter);

    /**
     * 신청 현황 확인
     */
    @Transactional(readOnly = true)
    List<OrderDto> myApply(String applicantId);

    /**
     * 같이구매 참여 취소
     */
    @Transactional
    void cancelPurchase(OrderInput parameter);
}
