package com.challenge.devchall.challange.repository;

import com.challenge.devchall.challange.dto.SettleChallengeDTO;
import com.challenge.devchall.challange.entity.Challenge;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {


    List<Challenge> findByChallengeStatus(boolean challengeStatus, Pageable pageable);

    List<Challenge> findByChallengeStatusAndIdNotIn(boolean challengeStatus, List<Long> challengeIds, Pageable pageable);
}
