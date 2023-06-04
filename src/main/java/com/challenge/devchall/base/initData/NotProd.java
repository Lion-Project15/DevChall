package com.challenge.devchall.base.initData;

import com.challenge.devchall.base.roles.ChallengeMember.Role;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ChallengeService challengeService,
            ChallengeMemberService challengeMemberService,
            ChallengePostService challengePostService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run (String... args) throws Exception {
                Member admin = memberService.join("admin", "1234", "admin@admin.com", "관리자", "관리자").getData();//admin 계정
                Member user1 = memberService.join("user1", "1234", "user1@devchall.com", "user1", "user1").getData();
                Member user2 = memberService.join("user2", "1234", "user2@devchall.com", "user2", "user2").getData();
                Member user3 = memberService.join("user3", "1234", "user3@devchall.com", "user3", "user3").getData();
                Member user4 = memberService.join("user4", "1234", "user4@devchall.com", "user4", "user4").getData();
                Member user5 = memberService.join("user5", "1234", "user5@devchall.com", "user5", "user5").getData();

                challengeService.createChallenge("1번 챌린지", "1번 챌린지 내용입니다", true, "day1",
                        "2023-06-01", "2주", "C", "개념 공부", "인증샷", admin);
                challengeService.createChallenge("2번 챌린지", "2번 챌린지 내용입니다", false, "day3",
                        "2023-06-01", "4주", "Java", "프로젝트", "IDE 캡처", user1);
                challengeService.createChallenge("3번 챌린지", "3번 챌린지 내용입니다", true, "day7",
                        "2023-06-01", "8주", "Python", "시험 대비", "Github", user2);
                challengeService.createChallenge("re 2번 챌린지", "re 2번 챌린지 내용입니다", false, "day3",
                        "2023-06-01", "4주", "Java", "프로젝트", "IDE 캡처", user5);

                challengeMemberService.addMember(challengeService.getChallengeById(2l), user3, Role.CREW);
                challengeMemberService.addMember(challengeService.getChallengeById(2l), admin, Role.CREW);
                challengeMemberService.addMember(challengeService.getChallengeById(4l), user1, Role.CREW);



                challengePostService.write("1-1인증", "1-1인증 내용입니다.", true, 3, 1, user1);
                challengePostService.write("1-2인증", "1-2인증 내용입니다.", false, 4, 1, user2);
                challengePostService.write("2-1인증", "2-1인증 내용입니다.", true, 5, 2, admin);
                challengePostService.write("2-2인증", "2-2인증 내용입니다.", false, 1, 2, user3);
                challengePostService.write("2-3인증", "2-3인증 내용입니다.", false, 1, 2, user1);
                challengePostService.write("2-4인증", "2-4인증 내용입니다.", false, 1, 2, user1);

                challengePostService.write("3-1인증", "3-1인증 내용입니다.", true, 2, 3, user4);
                challengePostService.write("3-2인증", "3-2인증 내용입니다.", true, 4, 3, user5);
                challengePostService.write("re2-1인증", "re2-1인증 내용입니다.", true, 4, 4, user1);
                challengePostService.write("re2-2인증", "re2-2인증 내용입니다.", true, 4, 4, user5);
            }
        };
    }
}