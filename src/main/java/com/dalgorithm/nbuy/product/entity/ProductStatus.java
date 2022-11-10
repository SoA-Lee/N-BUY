package com.dalgorithm.nbuy.product.entity;

public enum ProductStatus {
    PROGRESS("신청 가능"),FINISH("모집 완료");

    private String status;
    ProductStatus(String status) {
        this.status = status;
    }
}
