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
        @Query("SELECT cm.id as challengemember_id, COUNT(p.id) as count " +
            "FROM ChallengeMember cm " +
            "JOIN cm.linkedChallenge c " +
            "JOIN ChallengePost p ON cm.challenger = p.challenger AND p.linkedChallenge = c " +
            "WHERE c.endDate = :today " +
            "GROUP BY cm.id") // 챌린지 아이디 + 멤버 아이디
        List<SettleChallengeDTO> findChallengeMemberCountByEndDate(@Param("today") LocalDate today);

    List<Challenge> findByChallengeStatus(boolean challengeStatus, Pageable pageable);

    List<Challenge> findByChallengeStatusAndIdNotIn(boolean challengeStatus, List<Long> challengeIds, Pageable pageable);
}
