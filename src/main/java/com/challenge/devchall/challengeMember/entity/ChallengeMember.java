package com.challenge.devchall.challengeMember.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.base.roles.ChallengeMember.Role;
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

    private Long challengeTotalPoint;

//    private List<Role> challengerRole2;

    public void turnValid(){
        this.isValid = !isValid;
    }

    public void challengerRole(Role challengerRole){
        this.challengerRole = challengerRole;
    }
}