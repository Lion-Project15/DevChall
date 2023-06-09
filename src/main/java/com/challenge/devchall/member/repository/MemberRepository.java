package com.challenge.devchall.member.repository;

import com.challenge.devchall.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.OptionalInt;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginID(String loginID);
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);

}


