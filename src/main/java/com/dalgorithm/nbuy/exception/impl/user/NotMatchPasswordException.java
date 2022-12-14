package com.dalgorithm.nbuy.exception.impl.user;

import com.dalgorithm.nbuy.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotMatchPasswordException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "비밀번호가 일치하지 않습니다.";
    }
}
