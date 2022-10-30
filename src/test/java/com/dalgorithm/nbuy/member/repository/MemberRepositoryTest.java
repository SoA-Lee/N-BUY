package com.dalgorithm.nbuy.member.repository;

import com.dalgorithm.nbuy.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.dalgorithm.nbuy.member.entity.MemberRole.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Member 저장 테스트")
    void createMemberTest() {
        Member member = new Member();
        member.setUserId("nbuy");
        member.setUserName("hello");

        Member savedMember = memberRepository.save(member);

        assertEquals("nbuy", savedMember.getUserId());
        assertEquals("hello", savedMember.getUserName());
    }

    @Test
    @DisplayName("이메일 인증키 조회 테스트")
    void findByEmailAuthKeyTest() {
        Member member1 = new Member();
        member1.setUserId("hello");
        member1.setUserEmail("nbuy@gmail.com");
        member1.setEmailAuthYn(true);
        member1.setEmailAuthKey("emailAuth");

        Member savedMember = memberRepository.save(member1);
        Optional<Member> findMember = memberRepository.findByEmailAuthKey(savedMember.getEmailAuthKey());

        assertEquals("emailAuth", findMember.get().getEmailAuthKey());
    }

    @Test
    @DisplayName("이메일 조회 테스트")
    void findByEmailTest() {
        Member member = new Member();
        member.setUserId("nbuy");
        member.setUserEmail("nbuy@gmail.com");
        member.setUserName("hello");
        member.setEmailAuthKey("nbuynbuy");
        member.setRole(ROLE_USER);

        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findByUserEmail(savedMember.getUserEmail());

        assertEquals("nbuy@gmail.com", findMember.getUserEmail());
    }
}