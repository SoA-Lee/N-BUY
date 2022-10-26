package com.dalgorithm.nbuy.member.service.impl;

import com.dalgorithm.nbuy.exception.DomainException;
import com.dalgorithm.nbuy.exception.ErrorCode;
import com.dalgorithm.nbuy.member.components.MailComponents;
import com.dalgorithm.nbuy.member.dto.MemberDto;
import com.dalgorithm.nbuy.member.entity.Member;
import com.dalgorithm.nbuy.member.model.MemberInput;
import com.dalgorithm.nbuy.member.repository.MemberRepository;
import com.dalgorithm.nbuy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.dalgorithm.nbuy.exception.ErrorCode.EMAIL_AUTH_ALREADY_COMPLETE;
import static com.dalgorithm.nbuy.exception.ErrorCode.EMAIL_AUTH_KEY_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;

    @Override
    public void register(MemberInput memberInput) {

        Optional<Member> optionalMember = memberRepository.findById(memberInput.getUserId());

        if (optionalMember.isPresent()) {
            throw new DomainException(ErrorCode.MEMBER_USER_ID_ALREADY_EXIST);
        }

        String encPassword = BCrypt.hashpw(memberInput.getPassword(), BCrypt.gensalt());
        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
                .userId(memberInput.getUserId())
                .userName(memberInput.getUserName())
                .userEmail(memberInput.getUserEmail())
                .phone(memberInput.getPhone())
                .password(encPassword)
                .userRole(0)
                .regDt(LocalDateTime.now())
                .emailAuthYn(false)
                .emailAuthKey(uuid)
                .build();
        memberRepository.save(member);

        String email = memberInput.getUserEmail();
        String subject = "[N-BUY] 사이트 가입을 축하드립니다. ";
        String text = "<p>N-BUY 사이트 가입을 축하드립니다.<p><p>아래 링크를 클릭하셔서 가입을 완료 하세요.</p>"
                + "<div><a target='_blank' href='http://localhost:8080/member/email_auth?id=" + uuid + "'> 가입 완료 </a></div>";

        mailComponents.sendMail(email, subject, text);
    }

    @Override
    public void emailAuth(String uuid) {

        Member findMember = memberRepository.findByEmailAuthKey(uuid)
                .orElseThrow(() -> new DomainException(EMAIL_AUTH_KEY_NOT_FOUND));

        if (findMember.isEmailAuthYn()) {
            throw new DomainException(EMAIL_AUTH_ALREADY_COMPLETE);
        }

        findMember.setUserStatus(Member.MEMBER_STATUS_USE);
        findMember.setEmailAuthYn(true);
        findMember.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(findMember);
    }

    @Override
    public MemberDto detail(String userId) {

        Member findMember = memberRepository.findById(userId)
                .orElseThrow(() -> new DomainException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberDto.of(findMember);
    }

    @Override
    public void updateMember(MemberInput memberInput) {

        Member findMember = memberRepository.findById(memberInput.getUserId())
                .orElseThrow(() -> new DomainException(ErrorCode.MEMBER_NOT_FOUND));

        findMember.setPhone(memberInput.getPhone());
        findMember.setZipcode(memberInput.getZipcode());
        findMember.setAddr(memberInput.getAddr());
        findMember.setAddrDetail(memberInput.getAddrDetail());
        findMember.setUdtDt(LocalDateTime.now());

        memberRepository.save(findMember);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member findMember = memberRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보가 존재하지 않습니다."));

        if (Member.MEMBER_STATUS_REQ.equals(findMember.getUserStatus())) {
            throw new DomainException(ErrorCode.MEMBER_NOT_EMAIL_AUTH);
        }

        if (Member.MEMBER_STATUS_STOP.equals(findMember.getUserStatus())) {
            throw new DomainException(ErrorCode.MEMBER_STOP_USE);
        }

        if (Member.MEMBER_STATUS_WITHDRAW.equals(findMember.getUserStatus())) {
            throw new DomainException(ErrorCode.MEMBER_WITHDRAW);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (findMember.getUserRole() == 1) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new User(findMember.getUserId(), findMember.getPassword(), grantedAuthorities);
    }

}
