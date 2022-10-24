package com.dalgorithm.nbuy.member.service.impl;

import com.dalgorithm.nbuy.common.ServiceResult;
import com.dalgorithm.nbuy.member.components.MailComponents;
import com.dalgorithm.nbuy.member.dto.MemberDto;
import com.dalgorithm.nbuy.member.entity.Member;
import com.dalgorithm.nbuy.member.exception.MemberNotEmailAuthException;
import com.dalgorithm.nbuy.member.exception.MemberStopUserException;
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

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;

    @Override
    public boolean register(MemberInput memberInput) {

        Optional<Member> optionalMember = memberRepository.findById(memberInput.getUserId());
        if (optionalMember.isPresent()) {
            //현재 userId에 해당하는 데이터 존재
            return false;
        }

        String encPassword = BCrypt.hashpw(memberInput.getPassword(), BCrypt.gensalt());
        String uuid = UUID.randomUUID().toString();

        // input 값 못가져오는 부분 확인하기
        Member member = Member.builder()
                .userId(memberInput.getUserId())
                .userName(memberInput.getUserName())
                .userNickname(memberInput.getUserNickname())
                .phone(memberInput.getPhone())
                .password(encPassword)
                .userRole(0)
                .regDt(LocalDateTime.now())
                .emailAuthYn(false)
                .emailAuthKey(uuid)
                .build();
        memberRepository.save(member);

        String email = memberInput.getUserId();
        String subject = "[N-BUY] 사이트 가입을 축하드립니다. ";
        String text = "<p>N-BUY 사이트 가입을 축하드립니다.<p><p>아래 링크를 클릭하셔서 가입을 완료 하세요.</p>"
                + "<div><a target='_blank' href='http://localhost:8080/member/email_auth?id=" + uuid + "'> 가입 완료 </a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean emailAuth(String uuid) {

        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();

        if (member.isEmailAuthYn()) {
            return false;
        }

        member.setUserStatus(Member.MEMBER_STATUS_USE);
        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

    @Override
    public MemberDto detail(String userId) {

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) return null;

        Member member = optionalMember.get();
        return MemberDto.of(member);
    }

    @Override
    public ServiceResult updateMember(MemberInput memberInput) {

        String userId = memberInput.getUserId();

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if(!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        member.setPhone(memberInput.getPhone());
        member.setZipcode(memberInput.getZipcode());
        member.setAddr(memberInput.getAddr());
        member.setAddrDetail(memberInput.getAddrDetail());
        member.setUdtDt(LocalDateTime.now());

        memberRepository.save(member);

        return new ServiceResult();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findById(username);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (Member.MEMBER_STATUS_REQ.equals(member.getUserStatus())) {
            throw new MemberNotEmailAuthException("이메일 활성화 이후에 로그인을 해주세요.");
        }

        if (Member.MEMBER_STATUS_STOP.equals(member.getUserStatus())) {
            throw new MemberStopUserException("정지된 회원 입니다.");
        }

        if (Member.MEMBER_STATUS_WITHDRAW.equals(member.getUserStatus())) {
            throw new MemberStopUserException("탈퇴된 회원 입니다.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (member.getUserRole() == 1) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }

}
