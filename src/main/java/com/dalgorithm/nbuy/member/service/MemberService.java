package com.dalgorithm.nbuy.member.service;

import com.dalgorithm.nbuy.member.dto.MemberDto;
import com.dalgorithm.nbuy.member.entity.Member;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public interface MemberService {

    /**
     * 회원가입하기
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    void register(Member member);

    /**
     * uuid에 해당하는 계정을 활성화 함.
     */
    @Transactional
    void emailAuth(String uuid);

    /**
     * 회원 상세 정보
     */
    MemberDto detail(String userId);

    /**
     * 회원정보 수정
     */
    @Transactional
    void updateMember(MemberDto memberDto);

    /**
     * 회원을 탈퇴시켜 주는 로직
     */
    @Transactional
    void withdraw(String userId, String password);
}
