package com.dalgorithm.nbuy.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    MEMBER_NOT_EMAIL_AUTH("이메일 활성화가 되지 않은 계정입니다."),
    MEMBER_STOP_USE("사용이 정지된 회원입니다."),
    MEMBER_WITHDRAW("탈퇴한 회원입니다."),
    MEMBER_ALREADY_EXIST("해당 아이디는 사용 중 입니다."),

    MEMBER_NOT_FOUND("존재하지 않는 회원입니다.");


    private final String description;
}
