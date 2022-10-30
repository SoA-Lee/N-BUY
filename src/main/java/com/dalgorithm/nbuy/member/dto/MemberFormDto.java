package com.dalgorithm.nbuy.member.dto;

import lombok.*;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberFormDto {

    @Size(min = 3, max = 12)
    @NotBlank(message = "아이디는 필수 항목입니다.")
    private String userId;

    @NotBlank
    @Size(min = 1, max = 12)
    @NotBlank(message = "이름은 필수 항목입니다.")
    private String userName;

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String userEmail;

    @NotBlank(message = "전화번호는 필수 항목입니다.")
    @Size(min = 9, max = 20)
    private String phone;

    @Size(min = 4, max = 12)
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;
}
