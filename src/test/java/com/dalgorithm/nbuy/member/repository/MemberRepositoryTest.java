package com.dalgorithm.nbuy.member.repository;

import com.dalgorithm.nbuy.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Member 저장 테스트")

    void saveTest() {
        Member member = Member.builder()
                .userId("nbuy@gmail.com")
                .userName("pro")
                .build();

        Member savedMember = memberRepository.save(member);

        assertEquals("nbuy@gmail.com", savedMember.getUserId());
        assertEquals("pro", savedMember.getUserName());
    }

    @Test
    @DisplayName("findByEmailAuthKey 테스트")
    void findByEmailAuthKeyTest() {
        Member member = Member.builder()
                .userId("nbuy@gmail.com")
                .userName("pro")
                .emailAuthKey("nbuynbuy")
                .build();

        Member savedMember = memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findByEmailAuthKey(member.getEmailAuthKey());

        assertEquals("nbuynbuy", savedMember.getEmailAuthKey());
        assertEquals("nbuynbuy", findMember.get().getEmailAuthKey());
    }
}