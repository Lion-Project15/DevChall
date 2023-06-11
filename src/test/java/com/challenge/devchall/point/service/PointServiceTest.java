package com.challenge.devchall.point.service;


import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import com.challenge.devchall.point.entity.Point;
import com.challenge.devchall.point.repository.PointRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    @Autowired
    private ChallengeMemberService challengeMemberService;

    @Autowired
    private ChallengePostService challengePostService;

    @Test
    @WithUserDetails("user1")
    void createTest () {
        Optional<Point> p = pointRepository.findById(2L);
        assertThat(p.get().getCurrentPoint()).isEqualTo(500L);
        assertThat(p.get().getTotalPoint()).isEqualTo(1000L);

    }

    @Test
    @WithUserDetails("user1")
    void postTest () {


    }

    @Test
    @WithUserDetails("user1")
    void settle () {

    //날자 설정(5.22 ~ 6.5) 코드로 LocalDate ld = localdate.of(2023.5.22.0.0.0)
        // 챌린지 생성 <---여기서 막힘 코드를 못짬
        // 유저1 글 쓰기 (5.22)
        //날자 설정 (오늘)
        //addpoint? 유저1의 포인트 보여주기

    }

    @Test
    void calcPointFromPosts () {
    }

    @Test
    void checkAchievementRate () {
    }
}