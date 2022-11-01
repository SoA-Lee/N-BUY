package com.dalgorithm.nbuy.member.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberException extends RuntimeException {
    private MemberErrorCode memberErrorCode;
    private String errorMessage;

    public MemberException(MemberErrorCode memberErrorCode){
        this.memberErrorCode = memberErrorCode;
        this.errorMessage = memberErrorCode.getDescription();
    }
}
