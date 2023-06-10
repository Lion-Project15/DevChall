package com.challenge.devchall.pointHistory.repository;

import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.pointHistory.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findByMember(Member member);
}