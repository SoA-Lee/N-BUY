package com.dalgorithm.nbuy.exception.impl.category;

import com.dalgorithm.nbuy.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistCategoryException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.CONFLICT.value();
    }

    @Override
    public String getMessage() {
        return "이미 존재하는 카테고리입니다.";
    }
}
