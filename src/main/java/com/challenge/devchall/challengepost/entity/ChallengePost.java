package com.challenge.devchall.challengepost.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.comment.entity.Comment;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.photo.entity.Photo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToOne
    private Photo postPhoto;
    private int reportCount;
    private String creatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge linkedChallenge;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member challenger;

    public void modifyPost(String postTitle, String postContents, boolean postIsPublic){

        this.postTitle = postTitle;
        this.postContents = postContents;
        this.postIsPublic = postIsPublic;
    }

    public void setReportCount (int reportCount) {
        this.reportCount = reportCount;
    }

    //덧글
    @JsonIgnoreProperties({"challengePost"})
    @OneToMany(mappedBy = "challengePost",cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

}