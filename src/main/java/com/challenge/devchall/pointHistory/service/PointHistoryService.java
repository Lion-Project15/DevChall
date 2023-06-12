package com.challenge.devchall.pointHistory.service;

import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.point.entity.Point;
import com.challenge.devchall.point.repository.PointRepository;
import com.challenge.devchall.pointHistory.entity.PointHistory;
import com.challenge.devchall.pointHistory.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;
    private final PointRepository pointRepository;

    public List<PointHistory> getAllPointHistories() {
        return pointHistoryRepository.findAll();
    }

    public void addPointHistory(Member member, long point, String type) {

        PointHistory pointHistory = PointHistory.builder()
                .member(member)
                .point(point)
                .currentpoint(member.getPoint().getCurrentPoint())
                .type(type)
                .build();

        pointHistoryRepository.save(pointHistory);
    }

    public List<PointHistory> getPointHistoriesByMember(Member member) {
        return pointHistoryRepository.findByMember(member);
    }
}
