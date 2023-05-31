package com.challenge.devchall.challengeMember.repository;

import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeMemberRepository extends JpaRepository<ChallengeMember, Long> {

    Optional<ChallengeMember> findByChallenger(Member member);

    Optional<ChallengeMember> findByLinkedChallenge(Challenge challenge);

    Optional<ChallengeMember> findByLinkedChallengeAndChallenger(Challenge challenge, Member member);

}
