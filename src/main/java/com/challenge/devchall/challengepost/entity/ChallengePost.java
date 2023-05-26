package com.challenge.devchall.challengepost.entity;

import com.challenge.devchall.base.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
public class ChallengePost extends BaseEntity {

    private String postTitle;
    private String postContents;
    private boolean postIsPublic;

    //FIXME 일단 보류
//    private String postImg;
//    private boolean postModify;
//    private int postStarPoint;
//    private int challengeId;

}