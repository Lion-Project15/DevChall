package com.challenge.devchall.point.service;


import com.challenge.devchall.point.entity.Point;
import com.challenge.devchall.point.repository.PointRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional
class PointServiceTest {
    @Autowired
    private PointService pointService;
    @Autowired
    private PointRepository pointRepository;

    @Test
    @WithUserDetails("user1")
    void createTest () {
        Optional<Point> p = pointRepository.findById(2L);
        assertThat(p.get().getCurrentPoint()).isEqualTo(500L);
        assertThat(p.get().getTotalPoint()).isEqualTo(1000L);

    }

    @Test
    void settle () {


    }

    @Test
    void calcPointFromPosts () {
    }

    @Test
    void checkAchievementRate () {
    }
}