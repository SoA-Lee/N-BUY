package com.dalgorithm.nbuy.member.entity;

import com.dalgorithm.nbuy.member.dto.MemberFormDto;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "member")
@ToString
public class Member implements MemberCode {

    @Id
    private String userId;

    private String userName;

    @Column(unique = true)
    private String userEmail;

    private String password;
    private String phone;

    private LocalDateTime regDt;
    private LocalDateTime udtDt;

    private boolean emailAuthYn;
    private LocalDateTime emailAuthDt;

    @Column(unique = true)
    private String emailAuthKey;

    @Enumerated(EnumType.STRING)
    private MemberRole role;
    private String userStatus;

    private String zipcode;
    private String addr;
    private String addrDetail;

    public static Member createMember(MemberFormDto memberFormDto){
        String enPassword = BCrypt.hashpw(memberFormDto.getPassword(), BCrypt.gensalt());
        String uuid = UUID.randomUUID().toString();

        Member member = new Member();
        member.setUserId(memberFormDto.getUserId());
        member.setUserName(memberFormDto.getUserName());
        member.setUserEmail(memberFormDto.getUserEmail());
        member.setPhone(memberFormDto.getPhone());
        member.setPassword(enPassword);
        member.setRole(MemberRole.ROLE_USER);
        member.setRegDt(LocalDateTime.now());
        member.setEmailAuthYn(false);
        member.setEmailAuthKey(uuid);

        return member;
    }
}
