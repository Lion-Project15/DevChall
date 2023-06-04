package com.challenge.devchall.challange.dto;

import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SettleChallengeDto {
    private long challengeId;
    private ChallengeMember challengeMember;
    private int count;
}
