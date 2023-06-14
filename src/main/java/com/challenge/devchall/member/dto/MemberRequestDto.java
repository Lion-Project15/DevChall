package com.challenge.devchall.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,16}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~16자리여야 합니다.")
    private String loginID; //회원 로그인 id

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,16}$", message = "비밀번호는 4~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;


    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String  email;
}
