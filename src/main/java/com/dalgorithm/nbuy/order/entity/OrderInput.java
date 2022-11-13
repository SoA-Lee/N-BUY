package com.dalgorithm.nbuy.order.entity;

import lombok.Data;

@Data
public class OrderInput {

    long productId;
    String applicantId;

    long orderId;
}
