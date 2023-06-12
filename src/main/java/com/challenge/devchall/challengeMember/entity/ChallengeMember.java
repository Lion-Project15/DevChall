package com.challenge.devchall.challengeMember.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challengeMember.role.Role;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"linkedChallenge", "challenger"})
@Entity
@Getter
public class ChallengeMember extends BaseEntity {

    private boolean isValid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge linkedChallenge;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member challenger;

    @Enumerated(EnumType.STRING)
    private Role challengerRole;

    private int totalPostCount;
    //스케줄러 -> 매일 초기화
    //private int postLimit;

    //private Long challengeTotalPoint;
//    private List<Role> challengerRole2;

    public void turnValid(){
        this.isValid = !isValid;
    }

    public void challengerRole(Role challengerRole){
        this.challengerRole = challengerRole;
    }

//    public RsData<ChallengeMember> updatePostLimit(){
//
//        if(this.postLimit != 0){
//            return RsData.of("F-1", "오늘은 이미 포스트를 작성했습니다.");
//        }else{
//            this.postLimit++;
//            this.totalPostCount++;
//            return RsData.of("S-1", "포스트 작성이 가능합니다.");
//        }
//    }

    public void increaseTotal(){
        ++this.totalPostCount;
    }

}