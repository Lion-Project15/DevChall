package com.challenge.devchall.challange.repository;

import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengepost.dto.SettleChallengeDTO;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {


    List<Challenge> findByChallengeStatus(boolean challengeStatus, Pageable pageable);

    List<Challenge> findByChallengeStatusAndIdNotIn(boolean challengeStatus, List<Long> challengeIds, Pageable pageable);

    //language와 subject를 동적 쿼리를 이용해서 challenge list 검색 (page: 30)
    @Query("SELECT c FROM Challenge c " +
            "WHERE (:challengeLanguage IS NULL OR c.challengeLanguage = :challengeLanguage) " +
            "AND (:challengeSubject IS NULL OR c.challengeSubject = :challengeSubject) ")
    List<Challenge> findByConditions(@Param("challengeLanguage") String challengeLanguage,
                                     @Param("challengeSubject") String challengeSubject,
                                     Pageable pageable);
    @Query("SELECT c " +
            "FROM Challenge c " +
            "LEFT JOIN ChallengeMember cm ON cm.linkedChallenge = c AND cm.challenger = :me " +
            "WHERE cm.challenger = null " +
            "AND (:challengeLanguage IS NULL OR c.challengeLanguage = :challengeLanguage) " +
            "AND (:challengeSubject IS NULL OR c.challengeSubject = :challengeSubject) ") //
    List<Challenge> findChallengeByNotJoin(@Param("challengeLanguage") String challengeLanguage,
                                           @Param("challengeSubject") String challengeSubject,
                                           @Param("me") Member me,
                                           Pageable pageable);

}
