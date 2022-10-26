package com.dalgorithm.nbuy.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public DomainException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
