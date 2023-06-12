package com.challenge.devchall.challengepost.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.comment.entity.Comment;
import com.challenge.devchall.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private long postScore;
    private int reportCount;
    private String smallPhoto;
    private String largePhoto;
    private String creatorId;

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

    public void setReportCount (int reportCount) {
        this.reportCount = reportCount;

    }
    //덧글
    @JsonIgnoreProperties({"challengePost"})
    @OneToMany(mappedBy = "challengePost",cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

}