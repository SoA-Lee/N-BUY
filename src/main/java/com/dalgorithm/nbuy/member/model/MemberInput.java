package com.dalgorithm.nbuy.member.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class MemberInput {

    @NotBlank
    private String userId;

    @NotBlank
    private String userName;

    @NotBlank
    private String userEmail;

    @NotBlank
    private String phone;

    @NotBlank
    private String password;

    private String zipcode;
    private String addr;
    private String addrDetail;
}
