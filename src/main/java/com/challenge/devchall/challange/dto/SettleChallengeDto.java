package com.challenge.devchall.challange.dto;

import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import lombok.*;


public interface SettleChallengeDto {
    ChallengeMember getChallmem();
    Long getChallenge_id();
    Long getChallengemember_id();
    Long getCount();
}
