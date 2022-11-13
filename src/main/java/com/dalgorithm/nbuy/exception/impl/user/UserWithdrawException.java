package com.dalgorithm.nbuy.exception.impl.user;

import com.dalgorithm.nbuy.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UserWithdrawException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "탈퇴한 회원입니다.";
    }
}
