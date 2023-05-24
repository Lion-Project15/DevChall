package com.challenge.devchall.challange.repository;

import com.challenge.devchall.challange.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
