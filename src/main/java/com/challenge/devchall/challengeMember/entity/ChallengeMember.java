package com.challenge.devchall.challengeMember.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challengeMember.role.Role;
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
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"linkedChallenge", "challenger"})
@Entity
@Getter
public class ChallengeMember extends BaseEntity {

    private boolean isValid;


    private int outCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge linkedChallenge;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member challenger;

    @Enumerated(EnumType.STRING)
    private Role challengerRole;

    private int totalPostCount;

    private Long challengeTotalPoint;

    @Value("${custom.challenge.memberOutCount}")
    private int memberOutCount;




    public void turnValid(){
        this.isValid = false;
    }
    public void increaseOutCount() {
        this.outCount += 1;
        if (outCount > memberOutCount)
            turnValid();
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

    public void updateTotal(){
        this.totalPostCount = this.totalPostCount+1;
    }

}