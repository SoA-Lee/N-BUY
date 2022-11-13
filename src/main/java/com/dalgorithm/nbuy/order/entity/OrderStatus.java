package com.dalgorithm.nbuy.order.entity;

public enum OrderStatus {
    REQ("신청 완료"),
    CANCEL("취소"),
    FORCE_DELETE("등록자 및 관리자에 의한 상품 철회");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
