package com.dalgorithm.nbuy.member.service.impl;

import com.dalgorithm.nbuy.exception.impl.user.AlreadyExistUserException;
import com.dalgorithm.nbuy.exception.impl.user.EmailAuthAlreadyCompleteException;
import com.dalgorithm.nbuy.exception.impl.user.EmailAuthKeyNotFoundException;
import com.dalgorithm.nbuy.exception.impl.user.NotMatchPasswordException;
import com.dalgorithm.nbuy.member.components.MailComponents;
import com.dalgorithm.nbuy.member.dto.MemberDto;
import com.dalgorithm.nbuy.member.entity.Member;
import com.dalgorithm.nbuy.member.entity.MemberCode;
import com.dalgorithm.nbuy.member.repository.MemberRepository;
import com.dalgorithm.nbuy.member.service.MemberService;
import com.dalgorithm.nbuy.member.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;

    @Override
    public void register(Member member) {

        Optional<Member> findMember = memberRepository.findById(member.getUserId());

        if (findMember.isPresent()) {
            throw new AlreadyExistUserException();
        }

        sendRegisterAuthEmail(member);
        memberRepository.save(member);
    }

    @Override
    public void emailAuth(String uuid) {

        Member findMember = memberRepository.findByEmailAuthKey(uuid)
                .orElseThrow(EmailAuthKeyNotFoundException::new);

        if (findMember.isEmailAuthYn()) {
            throw new EmailAuthAlreadyCompleteException();
        }

        findMember.setUserStatus(Member.MEMBER_STATUS_USE);
        findMember.setEmailAuthYn(true);
        findMember.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(findMember);
    }

    @Override
    public MemberDto detail(String userId) {

        Member findMember = memberRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("회원정보가 존재하지 않습니다."));

        return MemberDto.fromEntity(findMember);
    }

    @Override
    public void updateMember(MemberDto memberDto) {

        Member findMember = memberRepository.findById(memberDto.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("회원정보가 존재하지 않습니다."));

        findMember.setPhone(memberDto.getPhone());
        findMember.setZipcode(memberDto.getZipcode());
        findMember.setAddr(memberDto.getAddr());
        findMember.setAddrDetail(memberDto.getAddrDetail());
        findMember.setUdtDt(LocalDateTime.now());

        memberRepository.save(findMember);
    }

    @Override
    public void withdraw(String userId, String password) {

        Member findMember = memberRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("회원정보가 존재하지 않습니다."));

        if (!PasswordUtils.equals(password, findMember.getPassword())) {
            throw  new NotMatchPasswordException();
        }

        findMember.setUserStatus(MemberCode.MEMBER_STATUS_WITHDRAW);
        memberRepository.save(findMember);
    }

    @Override
    public Page<MemberDto> searchMember(String keyword, Pageable pageable) {

        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize());

        if(StringUtils.hasText(keyword)) {
            return memberRepository.findByUserEmailContaining(keyword, pageable).map(MemberDto::fromEntity);
        }else{
            return memberRepository.findAll(pageable).map(MemberDto::fromEntity);
        }
    }

    @Override
    public void updateStatus(String userId, String userStatus) {

        Member findMember = memberRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보가 존재하지 않습니다."));

        findMember.setUserStatus(userStatus);

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
