package com.challenge.devchall.challange.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Challenge extends BaseEntity {

    private String challengeName;
    private String challengeContents;
    private boolean challengeStatus;
    private String challengeImg;
    private int challengeFrequency;
    private LocalDate startDate;
    private int challengePeriod;
    private String challengeLanguage;
    private String challengeSubject;
    private String challengePostType;

    @OneToMany(mappedBy = "linkedChallenge")
    private List<ChallengePost> challengePostList = new ArrayList<>();

}
