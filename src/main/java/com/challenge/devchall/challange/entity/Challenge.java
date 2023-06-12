package com.challenge.devchall.challange.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.photo.entity.Photo;
import com.challenge.devchall.pointHistory.entity.PointHistory;
import com.challenge.devchall.pointHistory.service.PointHistoryService;
import com.challenge.devchall.tag.entity.Tag;
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

    //둘 다 저장하지 않고, baseUrl만 저장 -> 필요할때마다 호출?
    @OneToOne
    private Photo challengePhoto;

    private int challengeFrequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private int challengePeriod;

    //Language, Subject, PostType을 하나로 묶어서 저장
    @OneToOne
    private Tag challengeTag;

    private long gatherPoints;
    private int challengeMemberLimit;
    private String challengeCreator;

    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default
    @OneToMany(mappedBy = "linkedChallenge")
    private List<ChallengePost> challengePostList = new ArrayList<>();

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
