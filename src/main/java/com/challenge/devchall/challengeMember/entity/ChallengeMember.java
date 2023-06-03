package com.challenge.devchall.challengeMember.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.base.roles.ChallengeMember.Role;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
public class ChallengeMember extends BaseEntity {

    private boolean isValid;

    //@ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    private Challenge linkedChallenge;

    @ManyToOne
    private Member challenger;

    @Enumerated(EnumType.STRING)
    private Role challengerRole;

    //스케줄러 -> 매일 초기화
    private int postLimit;

//    private List<Role> challengerRole2;

    public void turnValid(){
        this.isValid = !isValid;
    }

    public void challengerRole(Role challengerRole){
        this.challengerRole = challengerRole;
    }

    public void updatePostLimit(int postLimit){

        this.postLimit = postLimit;
    }


}