package com.dalgorithm.nbuy.exception.impl.order;

import com.dalgorithm.nbuy.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class RecruiterEqualApplicantException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "본인의 같이구매 상품에 대해서는 주문 신청이 불가능합니다.";
    }
}
