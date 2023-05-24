package com.challenge.devchall.challengepost.repository;

import com.challenge.devchall.challengepost.entity.ChallengePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengePostRepository extends JpaRepository<ChallengePost, Long> {
}
