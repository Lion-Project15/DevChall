package com.challenge.devchall.member.entity;

import com.challenge.devchall.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
public class Member extends BaseEntity {
    @Column(unique = true)
    private String loginID; //회원 로그인 id
    private String username; //본명
    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String  email;

    private String emailToken; //이메일 토큰
    private boolean isValid; // 인증 여부

    //role은 spring security 이후에 작성
    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        if ("admin".equals(loginID)) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }

        return grantedAuthorities;
    }



}