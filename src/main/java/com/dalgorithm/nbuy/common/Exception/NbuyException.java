package com.dalgorithm.nbuy.common.Exception;

import com.dalgorithm.nbuy.common.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NbuyException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public NbuyException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
