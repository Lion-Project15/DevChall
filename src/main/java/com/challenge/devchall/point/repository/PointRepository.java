package com.challenge.devchall.point.repository;

import com.challenge.devchall.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
