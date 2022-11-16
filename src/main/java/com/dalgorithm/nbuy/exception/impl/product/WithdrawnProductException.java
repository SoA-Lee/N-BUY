package com.dalgorithm.nbuy.exception.impl.product;

import com.dalgorithm.nbuy.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class WithdrawnProductException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "관리자 및 등록자에 의해 삭제된 상품입니다.";
    }
}
