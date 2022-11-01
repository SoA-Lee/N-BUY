package com.dalgorithm.nbuy.member.service.impl;

import com.dalgorithm.nbuy.member.exception.MemberErrorCode;
import com.dalgorithm.nbuy.member.components.MailComponents;
import com.dalgorithm.nbuy.member.dto.MemberDto;
import com.dalgorithm.nbuy.member.entity.Member;
import com.dalgorithm.nbuy.member.entity.MemberCode;
import com.dalgorithm.nbuy.member.exception.MemberException;
import com.dalgorithm.nbuy.member.repository.MemberRepository;
import com.dalgorithm.nbuy.member.service.MemberService;
import com.dalgorithm.nbuy.member.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.dalgorithm.nbuy.member.exception.MemberErrorCode.EMAIL_AUTH_ALREADY_COMPLETE;
import static com.dalgorithm.nbuy.member.exception.MemberErrorCode.EMAIL_AUTH_KEY_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;

    @Override
    public void register(Member member) {

        Member findMember = memberRepository.findByUserEmail(member.getUserEmail());

        if (findMember != null) {
            throw new MemberException(MemberErrorCode.MEMBER_ALREADY_REGISTER);
        }

        sendRegisterAuthEmail(member);
        memberRepository.save(member);
    }

    @Override
    public void emailAuth(String uuid) {

        Member findMember = memberRepository.findByEmailAuthKey(uuid)
                .orElseThrow(() -> new MemberException(EMAIL_AUTH_KEY_NOT_FOUND));

        if (findMember.isEmailAuthYn()) {
            throw new MemberException(EMAIL_AUTH_ALREADY_COMPLETE);
        }

        findMember.setUserStatus(Member.MEMBER_STATUS_USE);
        findMember.setEmailAuthYn(true);
        findMember.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(findMember);
    }

    @Override
    public MemberDto detail(String userId) {

        Member findMember = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        return MemberDto.of(findMember);
    }

    @Override
    public void updateMember(MemberDto memberDto) {

        Member findMember = memberRepository.findById(memberDto.getUserId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        findMember.setPhone(memberDto.getPhone());
        findMember.setZipcode(memberDto.getZipcode());
        findMember.setAddr(memberDto.getAddr());
        findMember.setAddrDetail(memberDto.getAddrDetail());
        findMember.setUdtDt(LocalDateTime.now());

        memberRepository.save(findMember);
    }

    @Override
    public void  withdraw(String userId, String password) {

        Member findMember = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!PasswordUtils.equals(password, findMember.getPassword())) {
            throw  new MemberException(MemberErrorCode.MEMBER_ID_PASSWORD_UNMATCH);
        }

        findMember.setUserName("삭제회원");
        findMember.setPhone("");
        findMember.setPassword("");
        findMember.setRegDt(null);
        findMember.setUdtDt(null);
        findMember.setEmailAuthYn(false);
        findMember.setEmailAuthDt(null);
        findMember.setEmailAuthKey("");
        findMember.setUserStatus(MemberCode.MEMBER_STATUS_WITHDRAW);
        findMember.setZipcode("");
        findMember.setAddr("");
        findMember.setAddrDetail("");
        memberRepository.save(findMember);
    }

    private void sendRegisterAuthEmail(Member member) {
        String email = member.getUserEmail();
        String subject = "[N-BUY] 사이트 가입을 축하드립니다. ";
        String text = "<p>N-BUY 사이트 가입을 축하드립니다.<p><p>아래 링크를 클릭하셔서 가입을 완료 하세요.</p>"
                + "<div><a target='_blank' href='http://localhost:8080/members/email_auth?id=" + member.getEmailAuthKey() + "'> 가입 완료 </a></div>";

        mailComponents.sendMail(email, subject, text);
    }

}
