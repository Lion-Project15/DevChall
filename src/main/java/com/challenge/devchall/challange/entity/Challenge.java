package com.challenge.devchall.challange.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
public class Challenge extends BaseEntity {
    private String challengeName;
    private String challengeContents;
    private boolean challengeStatus;
    private String challengeImg;
    private String challengeTag;
    private int challengeFrequency;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "linkedChallenge")
    private List<ChallengePost> challengePostList = new ArrayList<>();

}
