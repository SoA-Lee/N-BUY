package com.dalgorithm.nbuy.member.service.impl;

import com.dalgorithm.nbuy.exception.ErrorCode;
import com.dalgorithm.nbuy.member.entity.Member;
import com.dalgorithm.nbuy.member.exception.MemberException;
import com.dalgorithm.nbuy.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.dalgorithm.nbuy.member.entity.MemberCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    MockMvc mockMvc;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("로그인 실패 - 미가입 회원")
    void loginFailedByNotExistMemberTest() {
        // given
        given(memberRepository.findById(anyString()))
                .willReturn(Optional.empty());

        // when
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("userEmail"));

        // then
        assertEquals(exception.getMessage(), "회원 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 미인증")
    void loginFailedByNotEmailAuthTest() {
        // given
        Member member = Member.builder()
                .userId("mem")
                .userStatus(MEMBER_STATUS_REQ)
                .build();

        given(memberRepository.findById(anyString()))
                .willReturn(Optional.of(member));

        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> userDetailsService.loadUserByUsername(member.getUserId()));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.MEMBER_NOT_EMAIL_AUTH);
    }

    @Test
    @DisplayName("로그인 실패 - 정지된 계정")
    void loginFailedByStopIdTest() {
        // given
        Member member = Member.builder()
                .userId("mem")
                .userStatus(MEMBER_STATUS_STOP)
                .build();

        given(memberRepository.findById(anyString()))
                .willReturn(Optional.of(member));

        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> userDetailsService.loadUserByUsername(anyString()));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.MEMBER_STOP_USE);
    }

    @Test
    @DisplayName("로그인 실패 - 탈퇴 회원")
    void loginFailedByWithdrawMemberTest() {
        // given
        Member member = Member.builder()
                .userId("mem")
                .userStatus(MEMBER_STATUS_WITHDRAW)
                .build();

        given(memberRepository.findById(anyString()))
                .willReturn(Optional.of(member));

        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> userDetailsService.loadUserByUsername(anyString()));

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.MEMBER_WITHDRAW);
    }
}