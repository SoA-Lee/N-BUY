package com.dalgorithm.nbuy.member.service;

import com.dalgorithm.nbuy.member.dto.MemberDto;
import com.dalgorithm.nbuy.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 회원 검색
     */
    @Transactional(readOnly = true)
    Page<MemberDto> searchMember(String keyword, Pageable pageable);

    /**
     * 회원 상태 변경
     */
    @Transactional
    void updateStatus(String userId, String userStatus);
}
