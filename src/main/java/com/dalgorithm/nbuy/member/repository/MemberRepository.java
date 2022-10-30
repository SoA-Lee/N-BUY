package com.dalgorithm.nbuy.member.repository;

import com.dalgorithm.nbuy.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmailAuthKey(String emailAuthKey);
    Member findByUserEmail(String email);
}
