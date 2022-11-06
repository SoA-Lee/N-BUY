package com.dalgorithm.nbuy.admin.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum CategoryErrorCode {
    CATEGORY_NOT_FOUND(NOT_FOUND, "존재하지 않는 카테고리입니다."),
    CATEGORY_ALREADY_EXIST(CONFLICT, "이미 존재하는 카테고리입니다.");

    private final HttpStatus httpStatus;
    private final String description;
}
