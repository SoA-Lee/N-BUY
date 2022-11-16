package com.dalgorithm.nbuy.member.entity;

public interface MemberCode { // 테이블 의미

    // 현재 가입 요청 중
    String MEMBER_STATUS_REQ = "REQ";

    // 현재 이용중인 상태
    String MEMBER_STATUS_USE = "USE";

    // 현재 이용중인 계정 취소
    String MEMBER_STATUS_WITHDRAW = "WITHDRAW";
}
