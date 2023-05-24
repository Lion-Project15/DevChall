package com.challenge.devchall.challengeMember.repository;

import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeMemberRepository extends JpaRepository<ChallengeMember, Long> {
}
