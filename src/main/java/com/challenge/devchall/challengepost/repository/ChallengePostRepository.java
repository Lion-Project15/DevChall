package com.challenge.devchall.challengepost.repository;

import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengePostRepository extends JpaRepository<ChallengePost, Long> {
    List<ChallengePost> findByLinkedChallenge(Challenge challenge);
    List<ChallengePost> findByLinkedChallengeAndChallengerOrderByCreateDateDesc(Challenge challenge,Member member);
}
