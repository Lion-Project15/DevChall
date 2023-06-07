package com.challenge.devchall.challengepost.repository;

import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengePostRepository extends JpaRepository<ChallengePost, Long> {
    List<ChallengePost> findByLinkedChallenge(Challenge challenge);
}
