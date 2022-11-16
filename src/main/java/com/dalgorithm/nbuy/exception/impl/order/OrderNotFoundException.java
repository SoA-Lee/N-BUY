package com.dalgorithm.nbuy.exception.impl.order;

import com.dalgorithm.nbuy.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 주문 정보입니다.";
    }
}
