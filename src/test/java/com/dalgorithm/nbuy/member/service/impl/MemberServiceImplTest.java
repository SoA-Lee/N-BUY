package com.dalgorithm.nbuy.member.service.impl;

import com.dalgorithm.nbuy.member.exception.MemberException;
import com.dalgorithm.nbuy.exception.ErrorCode;
import com.dalgorithm.nbuy.member.entity.Member;
import com.dalgorithm.nbuy.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("이메일 인증 성공")
    void emailAuthTest() {

        Member member = new Member();
        member.setUserId("hello");
        member.setUserEmail("nbuy@gmail.com");
        member.setEmailAuthKey("emailAuth");
        member.setEmailAuthYn(false);

        // given
        given(memberRepository.findByEmailAuthKey(anyString()))
                .willReturn(Optional.of(member));

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        // when
        memberService.emailAuth(member.getEmailAuthKey());

        // then
        verify(memberRepository, times(1)).save(captor.capture());
        assertTrue(member.isEmailAuthYn());
        assertEquals("USE", captor.getValue().getUserStatus());
    }

    @Test
    @DisplayName("이메일 인증 실패 - 이메일 인증 키 불일치")
    void emailAuth_EmailAuthKeyNotFound() {

        Member findMember = new Member();
        findMember.setUserId("hello");
        findMember.setUserEmail("nbuy@gmail.com");
        findMember.setEmailAuthYn(false);
        findMember.setEmailAuthKey("emailAuth");

        // given
        given(memberRepository.findByEmailAuthKey(anyString()))
                .willReturn(Optional.empty());

        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.emailAuth(findMember.getEmailAuthKey()));

        // then
        assertEquals(ErrorCode.EMAIL_AUTH_KEY_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("이메일 인증 실패 - 이미 인증 완료된 이메일")
    void emailAuth_EmailAuthAlreadyComplete() {

        Member findMember = new Member();
        findMember.setUserId("hello");
        findMember.setUserEmail("nbuy@gmail.com");
        findMember.setEmailAuthYn(true);
        findMember.setEmailAuthKey("emailAuth");

        // given
        given(memberRepository.findByEmailAuthKey(anyString()))
                .willReturn(Optional.of(findMember));

        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.emailAuth(findMember.getEmailAuthKey()));

        // then
        assertEquals(ErrorCode.EMAIL_AUTH_ALREADY_COMPLETE, exception.getErrorCode());
    }
}