package com.dalgorithm.nbuy.member.service.impl;

import com.dalgorithm.nbuy.member.dto.MemberDto;
import com.dalgorithm.nbuy.member.entity.Member;
import com.dalgorithm.nbuy.member.exception.MemberErrorCode;
import com.dalgorithm.nbuy.member.exception.MemberException;
import com.dalgorithm.nbuy.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    void emailAuthSuccessTest() {

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
    void emailAuthFailedByEmailAuthKeyNotFound() {

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
        assertEquals(MemberErrorCode.EMAIL_AUTH_KEY_NOT_FOUND, exception.getMemberErrorCode());
    }

    @Test
    @DisplayName("이메일 인증 실패 - 이미 인증 완료된 이메일")
    void emailAuthFailedByEmailAuthAlreadyCompleteTest() {

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
        assertEquals(MemberErrorCode.EMAIL_AUTH_ALREADY_COMPLETE, exception.getMemberErrorCode());
    }

    @Test
    @DisplayName("마이페이지 조회 성공")
    void detailSuccessTest() {
        Member findMember = new Member();
        findMember.setUserId("hello");
        findMember.setUserEmail("nbuy@gmail.com");
        findMember.setUserName("엔바이");

        // given
        given(memberRepository.findById(anyString()))
                .willReturn(Optional.of(findMember));

        // when
        MemberDto memberDto = memberService.detail(findMember.getUserId());

        // then
        assertEquals("엔바이", memberDto.getUserName());
    }

    @Test
    @DisplayName("마이페이지 조회 실패 - 존재하지 않는 회원")
    void detailFailedByMemberNotFoundTest() {
        Member findMember = Member.builder()
                .userId("hello")
                .userEmail("nbuy@gmail.com")
                .userName("엔바이")
                .build();

        // given
        given(memberRepository.findById(anyString()))
                .willReturn(Optional.empty());

        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.detail(findMember.getUserId()));

        // then
        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, exception.getMemberErrorCode());
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void updateMemberSuccess() {
        String phone = "010-1234-5678";

        Member findMember = new Member();
        findMember.setUserId("hello");
        findMember.setUserEmail("nbuy@gmail.com");
        findMember.setUserName("엔바이");
        findMember.setPhone(phone);

        MemberDto memberDto = MemberDto.builder()
                .phone(phone)
                .build();

        // given
        given(memberRepository.findById(any()))
                .willReturn(Optional.of(findMember));

        // when
        memberService.updateMember(memberDto);

        // then
        assertEquals(findMember.getPhone(), phone);
        assertEquals(findMember.getUserId(), "hello");
    }

    @Test
    @DisplayName("회원 정보 수정 실패 - 존재하지 않는 회원")
    void updateMemberFailedByMemberNotFound() {
        MemberDto findMember = MemberDto.builder()
                .userId("hello")
                .userEmail("nbuy@gmail.com")
                .userName("엔바이")
                .build();

        // given
        given(memberRepository.findById(anyString()))
                .willReturn(Optional.empty());

        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.updateMember(findMember));

        // then
        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, exception.getMemberErrorCode());
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void withdrawSuccess() {
        Member findMember = Member.builder()
                .userId("mem")
                .password(BCrypt.hashpw("1234", BCrypt.gensalt()))
                .build();

        // given
        given(memberRepository.findById(anyString()))
                .willReturn(Optional.of(findMember));

        // when
        memberService.withdraw("mem", "1234");

        // then
        assertEquals(findMember.getUserStatus(), "WITHDRAW");
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 존재하지 않는 회원")
    void withdrawFailedByMemberNotFound() {

        Member findMember = Member.builder()
                .userId("mem")
                .password("1234")
                .build();

        // given
        given(memberRepository.findById(anyString()))
                .willReturn(Optional.empty());

        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.withdraw("mem", "1234"));

        // then
        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, exception.getMemberErrorCode());
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 아이디, 비밀번호 불일치")
    void withdrawFailedByIdPasswordUnmatch() {
        Member findMember = Member.builder()
                .userId("mem")
                .password(BCrypt.hashpw("1234", BCrypt.gensalt()))
                .build();

        // given
        given(memberRepository.findById(anyString()))
                .willReturn(Optional.of(findMember));

        // when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.withdraw("mem", "4321"));

        // then
        assertEquals(MemberErrorCode.MEMBER_ID_PASSWORD_UNMATCH, exception.getMemberErrorCode());
    }

}