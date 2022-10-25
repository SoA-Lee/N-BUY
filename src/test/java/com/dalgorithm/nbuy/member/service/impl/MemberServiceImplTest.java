package com.dalgorithm.nbuy.member.service.impl;

import com.dalgorithm.nbuy.member.entity.Member;
import com.dalgorithm.nbuy.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        Member member = Member.builder()
                .userId("nbuy")
                .emailAuthKey("emailAuth").build();

        // given
        given(memberRepository.findByEmailAuthKey(anyString()))
                .willReturn(Optional.of(member));

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        // when
        boolean result = memberService.emailAuth(member.getEmailAuthKey());

        // then
        verify(memberRepository, times(1)).save(captor.capture());
        assertTrue(result);
        assertTrue(member.isEmailAuthYn());
        assertEquals("USE", captor.getValue().getUserStatus());
    }
}