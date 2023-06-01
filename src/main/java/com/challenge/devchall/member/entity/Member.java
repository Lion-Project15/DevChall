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

    //role은 spring security 이후에 작성
    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        if ("admin".equals(loginID)) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }

        return grantedAuthorities;
    }

    //Member(내가) 어떤 챌린지에 들어가 있는지 확인하기 위해 작성했다.
    //Challenge(내가 참여한 챌린지 리스트)
    //n:n 방식이라면 -> 중간 테이블 필요 -> ChallengeMember
    //Challenge랑 연결하기 위해 -> 중간 테이블 challengeMember와 연결
    //ChallengeMember는 Challenge를 갖고 있기 때문에 조회가능

    //우리의 목적은 Challenge다.
    //Challenge 를 그대로 가지고 있으면 이걸 출력만 하면 되는데
    //아래의 방식도 가능하기는 하지만 ChallengeMember(일단 내가 있는지부터 확인) -> Challenge



}