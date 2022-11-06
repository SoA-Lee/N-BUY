package com.dalgorithm.nbuy.member.dto;

import com.dalgorithm.nbuy.member.entity.Member;
import com.dalgorithm.nbuy.member.entity.MemberRole;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

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

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .userId(member.getUserId())
                .userName(member.getUserName())
                .userEmail(member.getUserEmail())
                .phone(member.getPhone())
                .password(member.getPassword())
                .regDt(member.getRegDt())
                .udtDt(member.getUdtDt())
                .emailAuthDt(member.getEmailAuthDt())
                .emailAuthDt(member.getEmailAuthDt())
                .role(member.getRole())
                .userStatus(member.getUserStatus())
                .zipcode(member.getZipcode())
                .addr((member.getAddr()))
                .addrDetail(member.getAddrDetail())
                .build();
    }
}
