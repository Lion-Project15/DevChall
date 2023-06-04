package com.challenge.devchall.challange.repository;

import com.challenge.devchall.challange.dto.SettleChallengeDto;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
        @Query("SELECT c.id, cm, COUNT(p.id) " +
            "FROM ChallengeMember cm " +
            "JOIN cm.linkedChallenge c " +
            "JOIN ChallengePost p ON cm.challenger = p.challenger " +
            "WHERE c.endDate = :today " +
            "GROUP BY c.id, cm.challenger")
    List<SettleChallengeDto> findChallengeMemberCountByEndDate(@Param("today") LocalDate today);

}
