package com.challenge.devchall.base.initData;

import com.challenge.devchall.base.Util.TestUtil;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.role.Role;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import com.challenge.devchall.item.service.ItemService;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import com.challenge.devchall.photo.entity.Photo;
import com.challenge.devchall.photo.service.PhotoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ChallengeService challengeService,
            ChallengeMemberService challengeMemberService,
            ChallengePostService challengePostService,
            ItemService itemService,
            PhotoService photoService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run (String... args) throws Exception {

                itemService.create("L-F-FCE411","font","FCE411", "000000",300);
                itemService.create("basic","font","3D4451", "FFFFFF",0);
                itemService.create("ED3096","font","ED3096", "FFFFFF",0);
                itemService.create("FF9900","font","FF9900", "000000",0);
                itemService.create("1144FC","font","1144FC", "FFFFFF",0);
                itemService.create("3CB24F","font","3CB24F", "FFFFFF",0);


                itemService.create("cow.png","character","http://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/cow.png?type=m&w=120&h=120&bgcolor=EBEBEB", " ", 0);
                itemService.create("lion.png","character","http://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/lion.png?type=m&w=120&h=120&bgcolor=EBEBEB", " ", 100);
                itemService.create("giraffe.png","character","http://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/giraffe.png?type=m&w=120&h=120&bgcolor=EBEBEB", " ", 0);
                itemService.create("robot.png","character","http://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/robot.png?type=m&w=120&h=120&bgcolor=EBEBEB", " ", 0);
                itemService.create("ghost.png","character","http://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/ghost.png?type=m&w=120&h=120&bgcolor=EBEBEB", " ", 0);
                itemService.create("octopus.png","character","http://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/octopus.png?type=m&w=120&h=120&bgcolor=EBEBEB", " ", 0);

                Member admin = memberService.join("admin", "1234", "admin@admin.com",  "관리자").getData();//admin 계정
                Member user1 = memberService.join("user1", "1234", "user1@devchall.com",  "user1" ).getData();
                Member user2 = memberService.join("user2", "1234", "user2@devchall.com", "user2" ).getData();
                Member user3 = memberService.join("user3", "1234", "user3@devchall.com",  "user3").getData();
                Member user4 = memberService.join("user4", "1234", "user4@devchall.com", "user4").getData();
                Member user5 = memberService.join("user5", "1234", "user5@devchall.com",  "user5").getData();
                Member user6 = memberService.join("user6", "1234", "user6@devchall.com",  "user6").getData();
                Member user7 = memberService.join("user6", "1234", "user6@devchall.com",  "user7").getData();
                Member user8 = memberService.join("user6", "1234", "user6@devchall.com",  "user8").getData();
                admin.getPoint().add(100000000);

                //미정 소셜 로그인정보
//                Member memberUserMJByKakao = memberService.whenSocialLogin("KAKAO", "KAKAO__2824935881","mijeong1015@naver.com","미정").getData();
//                Member memberUserMJByNaver = memberService.whenSocialLogin("NAVER", "NAVER__nydxlR1MJUEOU2XKgEWh4nhLSWUAE9eIXR5ae8oOAbQ").getData();

                String photoUrl = "https://kr.object.ncloudstorage.com/devchall/devchall_img/example1.png";
                Photo photo = photoService.createPhoto(photoUrl);

                //FIXME createChallengeForNotProd 를 작성하면 안될 것 같음 (중복코드) => Multipartfile을 어떻게 처리할 것인가...
                Challenge c1 = challengeService.createChallengeForNoPhoto("1번 챌린지", "1번 챌린지 내용입니다", true, 1, LocalDate.now().plusDays(5), 2, "C", "개념 공부", "인증샷", admin);
                Challenge c2 = challengeService.createChallengeForNoPhoto("멤버들이 모여있는 2번 챌린지", "2번 챌린지 내용입니다", true, 3,LocalDate.now().plusDays(5), 4, "Java", "프로젝트", "IDE 캡처", user1);
                Challenge c3 = challengeService.createChallengeForNoPhoto("3번 챌린지", "3번 챌린지 내용입니다", true, 7, LocalDate.now().plusDays(5), 8, "Python", "시험 대비", "Github", user2);
                Challenge c4 = challengeService.createChallengeForNoPhoto("re 2번 챌린지", "re 2번 챌린지 내용입니다", false, 3,LocalDate.now().plusDays(5), 4, "Java", "프로젝트", "IDE 캡처", user5);
                for(int i=0; i<20; i++){
                    challengeService.createChallengeForNoPhoto(i+"th 테스트 챌린지", i+"번 테스트 챌린지 내용입니다", true, 1, LocalDate.now().plusDays(5), 2, "C", "개념 공부", "인증샷", admin);
                }
                //test 0 챌린지의 최대 인원을 1로 변경
                TestUtil.setFieldValue(challengeService.getChallengeById(5), "challengeMemberLimit", 1);

                challengeMemberService.addMember(c1, user1, Role.CREW);
                challengeMemberService.addMember(c1, user2, Role.CREW);
                challengeMemberService.addMember(c2, user2, Role.CREW);
                challengeMemberService.addMember(c2, user3, Role.CREW);
                challengeMemberService.addMember(c2, admin, Role.CREW);
                challengeMemberService.addMember(c3, user4, Role.CREW);
                challengeMemberService.addMember(c3, user5, Role.CREW);
                challengeMemberService.addMember(c4, user1, Role.CREW);

                challengePostService.write("1-1인증", "1-1인증 내용입니다.", true, 3, c1.getId(), photoUrl, admin);
                challengePostService.write("1-2인증", "1-2인증 내용입니다.", false, 4, c1.getId(), photoUrl, user2);
                challengePostService.write("2-1인증", "2-1인증 내용입니다.", true, 5, c2.getId(), photoUrl, admin);
                challengePostService.write("2-2인증", "2-2인증 내용입니다.", false, 1, c2.getId(), photoUrl, user3);
                challengePostService.write("2-3인증", "2-3인증 내용입니다.", false, 1, c2.getId(), photoUrl, user1);
                challengePostService.write("2-4인증", "2-4인증 내용입니다.", false, 1, c2.getId(), photoUrl, user1);
                challengePostService.write("3-1인증", "3-1인증 내용입니다.", true, 2, c3.getId(), photoUrl, user4);
                challengePostService.write("3-2인증", "3-2인증 내용입니다.", true, 4, c3.getId(), photoUrl, user5);
                challengePostService.write("re2-1인증", "re2-1인증 내용입니다.", true, 4, c4.getId(), photoUrl, user1);
                challengePostService.write("re2-2인증", "re2-2인증 내용입니다.", true, 4, c4.getId(), photoUrl, user5);
                for(int i=0 ; i<20; i++){
                    List<ChallengePost> cps = challengePostService.getRecentPosts(c2, admin);
                    TestUtil.setFieldValue(cps.get(0), "createDate", LocalDateTime.now().minusDays(10));
                    challengePostService.write("인증 테스트"+i, i+"인증 내용입니다.", true, 3, c2.getId(), photoUrl, admin);
                }
            }
        };
    }
}