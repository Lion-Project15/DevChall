package com.challenge.devchall.challengepost.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ChallengePost extends BaseEntity {
    @CreatedDate
    private LocalDateTime createDate;
    private String postTitle;
    private String postContents;
    private boolean postIsPublic;
    private long postScore;
    private String smallPhoto;
    private String largePhoto;

    //FIXME 일단 보류
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

}