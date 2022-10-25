package com.dalgorithm.nbuy.member.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MemberInput {

    private String userId;
    private String userName;
    private String userEmail;
    private String phone;
    private String password;

    private String zipcode;
    private String addr;
    private String addrDetail;
}
