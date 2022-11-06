package com.dalgorithm.nbuy.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum MemberErrorCode {

    /*
        400 BAD_REQUEST : 잘못된 요청
        401 UNAUTHORIZED : 인증되지 않은 사용자
        403 FORBIDDEN : 접근 금지
        404 NOT_FOUND : Resource 를 찾을 수 없음
        409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재
     */

    MEMBER_NOT_EMAIL_AUTH(UNAUTHORIZED, "이메일 활성화가 되지 않은 계정입니다."),
    MEMBER_STOP_USE(NOT_FOUND, "사용이 정지된 회원입니다."),
    MEMBER_WITHDRAW(NOT_FOUND,"탈퇴한 회원입니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "회원정보가 존재하지 않습니다."),
    MEMBER_USER_ID_ALREADY_EXIST(CONFLICT, "해당 아이디는 사용 중 입니다."),
    MEMBER_ALREADY_REGISTER(CONFLICT,"이미 가입된 회원입니다."),

    MEMBER_ID_PASSWORD_UNMATCH(NOT_FOUND, "비밀번호가 일치하지 않습니다."),

    EMAIL_AUTH_ALREADY_COMPLETE(CONFLICT, "이메일 인증이 이미 완료된 상태입니다."),
    EMAIL_AUTH_KEY_NOT_FOUND(NOT_FOUND, "존재하지 않는 이메일 인증 키입니다.");

    private final HttpStatus httpStatus;
    private final String description;
}
