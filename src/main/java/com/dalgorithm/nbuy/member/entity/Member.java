package com.dalgorithm.nbuy.member.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "member")
public class Member implements MemberCode {

    @Id
    private String userId;

    private String userName;
    private String userEmail;
    private String password;
    private String phone;

    private LocalDateTime regDt;
    private LocalDateTime udtDt;

    private boolean emailAuthYn;
    private LocalDateTime emailAuthDt;
    private String emailAuthKey;

    private int userRole;
    private String userStatus;

    private String zipcode;
    private String addr;
    private String addrDetail;
}
