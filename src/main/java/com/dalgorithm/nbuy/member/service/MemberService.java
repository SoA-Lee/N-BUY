package com.dalgorithm.nbuy.member.service;

import com.dalgorithm.nbuy.common.ServiceResult;
import com.dalgorithm.nbuy.member.dto.MemberDto;
import com.dalgorithm.nbuy.member.model.MemberInput;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public interface MemberService extends UserDetailsService {

    /**
     * 회원가입하기
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    boolean register(MemberInput memberInput);

    /**
     * uuid에 해당하는 계정을 활성화 함.
     */
    @Transactional
    boolean emailAuth(String uuid);

    /**
     * 회원 상세 정보
     */
    MemberDto detail(String userId);

    /**
     * 회원정보 수정
     */
    @Transactional
    ServiceResult updateMember(MemberInput memberInput);
}
