package com.dalgorithm.nbuy.member.dto;

import com.dalgorithm.nbuy.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    String userId;
    String userName;
    String userNickname;
    String phone;
    String password;
    LocalDateTime regDt;
    LocalDateTime udtDt;

    String emailAuthKey;
    boolean emailAuthYn;
    LocalDateTime emailAuthDt;

    int userRole;
    String userStatus;

    private String zipcode;
    private String addr;
    private String addrDetail;

    // 추가 컬럼
    long totalCount;
    // 시퀀스 처리
    long seq;

    public static MemberDto of(Member member) {
        return MemberDto.builder()
                .userId(member.getUserId())
                .userName(member.getUserName())
                .userNickname(member.getUserNickname())
                .phone(member.getPhone())
                .password(member.getPassword())
                .regDt(member.getRegDt())
                .emailAuthYn(member.isEmailAuthYn())
                .emailAuthDt(member.getEmailAuthDt())
                .emailAuthKey(member.getEmailAuthKey())
                .userRole(member.getUserRole())
                .userStatus(member.getUserStatus())
                .zipcode(member.getZipcode())
                .addr(member.getAddr())
                .addrDetail(member.getAddrDetail())
                .build();
    }

    public String getRegDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return regDt != null ? regDt.format(formatter) : "";
    }

    public String getUdtDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return udtDt != null ? udtDt.format(formatter) : "";

    }
}
