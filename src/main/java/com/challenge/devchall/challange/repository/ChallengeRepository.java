package com.challenge.devchall.challange.repository;

import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {


    List<Challenge> findByChallengeStatus(boolean challengeStatus, Pageable pageable);

    List<Challenge> findByChallengeStatusAndIdNotIn(boolean challengeStatus, List<Long> challengeIds, Pageable pageable);
}
