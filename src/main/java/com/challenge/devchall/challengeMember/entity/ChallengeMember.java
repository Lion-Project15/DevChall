package com.challenge.devchall.challengeMember.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.base.roles.ChallengeMember.Role;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.repository.ChallengeMemberRepository;
import com.challenge.devchall.comment.entity.Comment;
import com.challenge.devchall.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"linkedChallenge", "challenger"})
@Entity
@Getter
public class ChallengeMember extends BaseEntity {

    private boolean isValid;

    @ManyToOne(cascade = CascadeType.ALL)
    private Challenge linkedChallenge;

    @ManyToOne
    private Member challenger;

    @Enumerated(EnumType.STRING)
    private Role challengerRole;

    //스케줄러 -> 매일 초기화
    private int postLimit;
    private int totalPostCount;
    private Long challengeTotalPoint;

    public void turnValid(){
        this.isValid = !isValid;
    }

    public void challengerRole(Role challengerRole){
        this.challengerRole = challengerRole;
    }

    public RsData<ChallengeMember> updatePostLimit(){

        if(this.postLimit != 0){
            return RsData.of("F-1", "오늘은 이미 포스트를 작성했습니다.");
        }else{
            this.postLimit++;
            this.totalPostCount++;
            return RsData.of("S-1", "포스트 작성이 가능합니다.");
        }
    }

}