package com.challenge.devchall.member.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String loginID; //회원 로그인 id
    private String username; //본명
    private String password;
    private String repeatPassword;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    private String emailToken; //이메일 토큰
    private boolean isValid; // 인증 여부

    @OneToMany(mappedBy = "challenger")
    private List<ChallengeMember> challengeMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "challenger")
    private List<ChallengePost> myPostList = new ArrayList<>();

    //스케줄러 -> 매달 1일에 0으로 초기화 되어야함.
    private int challengeLimit;

    //role은 spring security 이후에 작성
    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        if ("admin".equals(loginID)) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }

        return grantedAuthorities;
    }

    //FIXME 세터 말고 다른 방법?
    public void setChallengeLimit(int challengeLimit){

        this.challengeLimit = challengeLimit;
    }

}