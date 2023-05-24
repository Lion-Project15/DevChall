package com.challenge.devchall.member.repository;

import com.challenge.devchall.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
