package com.challenge.devchall.challange.entity;

import com.challenge.devchall.base.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

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
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
