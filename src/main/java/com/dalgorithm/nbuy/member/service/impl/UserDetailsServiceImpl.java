package com.dalgorithm.nbuy.member.service.impl;

import com.dalgorithm.nbuy.exception.ErrorCode;
import com.dalgorithm.nbuy.member.entity.Member;
import com.dalgorithm.nbuy.member.entity.MemberRole;
import com.dalgorithm.nbuy.member.exception.MemberException;
import com.dalgorithm.nbuy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member findMember = memberRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보가 존재하지 않습니다."));

        if (Member.MEMBER_STATUS_REQ.equals(findMember.getUserStatus())) {
            throw new MemberException(ErrorCode.MEMBER_NOT_EMAIL_AUTH);
        }

        if (Member.MEMBER_STATUS_STOP.equals(findMember.getUserStatus())) {
            throw new MemberException(ErrorCode.MEMBER_STOP_USE);
        }

        if (Member.MEMBER_STATUS_WITHDRAW.equals(findMember.getUserStatus())) {
            throw new MemberException(ErrorCode.MEMBER_WITHDRAW);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (findMember.getRole() == MemberRole.ROLE_ADMIN) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new User(findMember.getUserId(), findMember.getPassword(), grantedAuthorities);
    }
}
