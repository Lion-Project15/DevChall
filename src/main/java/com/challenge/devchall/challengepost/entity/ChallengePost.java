package com.challenge.devchall.challengepost.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ChallengePost extends BaseEntity {

    private String postTitle;
    private String postContents;
    private boolean postIsPublic;

    //FIXME 일단 보류
//    private String postImg;
//    private boolean postModify;
//    private int postStarPoint;

    ////@ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    private Challenge linkedChallenge;

    @ManyToOne
    private Member challenger;

    public void modifyPost(String postTitle, String postContents, boolean postIsPublic){

        this.postTitle = postTitle;
        this.postContents = postContents;
        this.postIsPublic = postIsPublic;

    }

    public boolean isPublic(){

        if(this.postIsPublic){
            return true;
        }
        else {
            return false;
        }

    }

    public OffsetDateTime getPostDate() {
        return OffsetDateTime.now();
    }
}