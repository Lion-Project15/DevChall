package com.challenge.devchall.point.service;

import com.challenge.devchall.point.entity.Point;
import com.challenge.devchall.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;

    public Point create(){
        return pointRepository.save(Point.builder()
                .currentPoint(1000L)
                .totalPoint(1000L)
                .build());
    }
}
