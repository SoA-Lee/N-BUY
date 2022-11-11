package com.dalgorithm.nbuy.exception.impl.user;

import com.dalgorithm.nbuy.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class EmailAuthNotYetException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이메일 활성화가 되지 않은 계정입니다.";
    }
}
