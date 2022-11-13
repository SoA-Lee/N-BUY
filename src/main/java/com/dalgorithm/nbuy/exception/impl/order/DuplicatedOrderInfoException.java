package com.dalgorithm.nbuy.exception.impl.order;

import com.dalgorithm.nbuy.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class DuplicatedOrderInfoException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.CONFLICT.value();
    }

    @Override
    public String getMessage() {
        return "해당 상품에 대해 중복된 신청 정보가 존재합니다.";
    }
}
