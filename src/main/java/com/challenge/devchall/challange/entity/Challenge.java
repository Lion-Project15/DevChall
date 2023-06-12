package com.challenge.devchall.challange.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.comment.entity.Comment;
import com.challenge.devchall.pointHistory.entity.PointHistory;
import com.challenge.devchall.pointHistory.service.PointHistoryService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"challengePostList"})
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Challenge extends BaseEntity {

    private String challengeName;
    private String challengeContents;
    private boolean challengeStatus;
    private String largePhoto;
    private String smallPhoto;
    private int challengeFrequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private int challengePeriod;
    private String challengeLanguage;
    private String challengeSubject;
    private String challengePostType;
    private long gatherPoints;
    private int challengeMemberLimit;

    private String challengeCreator;
    private String photoFile;
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default
    @OneToMany(mappedBy = "linkedChallenge")
    private List<ChallengePost> challengePostList = new ArrayList<>();

    //덧글
    @JsonIgnoreProperties({"challenge"})
    @OneToMany(mappedBy = "challenge",cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Comment> commentList;

    public void addPoint(long points){
        this.gatherPoints+=points;
    }
    public void subtractPoint(long points){
        this.gatherPoints-=points;
    }
    public void resetPoint(){
        this.gatherPoints = 0;
    }

}
