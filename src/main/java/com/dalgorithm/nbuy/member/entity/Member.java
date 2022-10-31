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

        return Member.builder()
                .userId(memberFormDto.getUserId())
                .userName(memberFormDto.getUserName())
                .userEmail(memberFormDto.getUserEmail())
                .phone(memberFormDto.getPhone())
                .password(enPassword)
                .role(MemberRole.ROLE_USER)
                .userStatus(MEMBER_STATUS_REQ)
                .regDt(LocalDateTime.now())
                .emailAuthYn(false)
                .emailAuthKey(uuid)
                .build();
    }
}
