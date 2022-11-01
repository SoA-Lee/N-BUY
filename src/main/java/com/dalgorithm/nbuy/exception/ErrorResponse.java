package com.dalgorithm.nbuy.exception;

import com.dalgorithm.nbuy.member.exception.MemberErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(MemberErrorCode memberErrorCode) {
        return ResponseEntity
                .status(memberErrorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(memberErrorCode.getHttpStatus().value())
                        .error(memberErrorCode.getHttpStatus().name())
                        .code(memberErrorCode.name())
                        .message(memberErrorCode.getDescription())
                        .build()
                );
    }
}