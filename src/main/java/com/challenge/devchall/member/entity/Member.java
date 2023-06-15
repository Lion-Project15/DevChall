package com.challenge.devchall.member.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.inventory.entity.Inventory;
import com.challenge.devchall.point.entity.Point;
import com.challenge.devchall.pointHistory.entity.PointHistory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = false, exclude = {"challengeMemberList", "myPostList", "pointHistories", "inventoryList"})
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String loginID; //회원 로그인 id
    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    private String providerTypeCode; //소셜 회원 = (NAVER or KAKAO), 일반회원 = (DevChall)

    @OneToOne(cascade = CascadeType.ALL) //영속화 작업
    @JoinColumn(referencedColumnName = "id") //데이터베이스의 point id와 join
    private Point point;

    @OneToMany(mappedBy = "challenger")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default
    private List<ChallengeMember> challengeMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "challenger")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default
    private List<ChallengePost> myPostList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default
    private List<PointHistory> pointHistories = new ArrayList<>();

    //스케줄러 -> 매달 1일에 0으로 초기화 되어야함.
    private int challengeLimit;


    @OneToMany(mappedBy = "member")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default
    private List<Inventory> inventoryList = new ArrayList<>();

    //FIXME 최대 참여 갯수도 추가해야함

    //role은 spring security 이후에 작성
    public List<? extends GrantedAuthority>getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        if ("admin".equals(loginID)) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }

        return grantedAuthorities;
    }

    public void setChallengeLimit(){
        this.challengeLimit++;
    }

    public void resetChallengeLimit(){
        this.challengeLimit = 0;
    }

    public Inventory getEquippedFont(){
        for(Inventory iv: inventoryList){
            if(iv.isEquipped() && //장착중
                    iv.getItem().getType().equals("font")){ //타입=폰트
                return iv;
            }
        }
        return null;
    }
    public Inventory getEquippedCharacter(){
        for(Inventory iv: inventoryList){
            if(iv.isEquipped() && //장착중
                    iv.getItem().getType().equals("character")){ //타입=캐릭터
                return iv;
            }
        }
        return null;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

}