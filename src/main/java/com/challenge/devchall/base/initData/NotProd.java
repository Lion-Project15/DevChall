package com.challenge.devchall.base.initData;

import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ChallengeService challengeService,
            ChallengePostService challengePostService
    ) {
        return args -> {
            Member admin = memberService.join("admin", "1234", "admin@admin.com", "관리자", "관리자");//admin 계정
            Member user1 = memberService.join("user1", "1234", "user1@devchall.com", "user1", "user1");
            Member user2 = memberService.join("user2", "1234", "user2@devchall.com", "user2", "user2");
            Member user3 = memberService.join("user3", "1234", "user3@devchall.com", "user3", "user3");
            Member user4 = memberService.join("user4", "1234", "user4@devchall.com", "user4", "user4");

            challengeService.createChallenge("1번 챌린지", "1번 챌린지 내용입니다", "public", "day1",
                    "2023-06-01", "2023-06-30", "C", "개념 공부", "인증샷", admin);
            challengeService.createChallenge("2번 챌린지", "2번 챌린지 내용입니다", "private", "day3",
                    "2023-06-01", "2023-06-30", "Java", "프로젝트", "IDE 캡처", user1);
            challengeService.createChallenge("3번 챌린지", "3번 챌린지 내용입니다", "public", "day7",
                    "2023-06-01", "2023-06-30", "Python", "시험 대비", "Github", user2);

            challengePostService.write("1-1인증", "1-1인증 내용입니다.", true, 3, 1);
            challengePostService.write("1-2인증", "1-2인증 내용입니다.", false, 4, 1);
            challengePostService.write("2-1인증", "2-1인증 내용입니다.", true, 5, 2);
            challengePostService.write("2-2인증", "2-2인증 내용입니다.", false, 1, 2);
            challengePostService.write("3-1인증", "3-1인증 내용입니다.", true, 2, 3);
            challengePostService.write("3-2인증", "3-2인증 내용입니다.", true, 4, 3);

        };
    }
}