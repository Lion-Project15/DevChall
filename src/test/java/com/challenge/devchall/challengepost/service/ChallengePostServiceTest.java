package com.challenge.devchall.challengepost.service;

import com.challenge.devchall.base.Util.TestUtil;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.role.Role;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional
class ChallengePostServiceTest {
    @Autowired
    private ChallengeMemberService challengeMemberService;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ChallengePostService challengePostService;

    @Test
    @WithUserDetails("user1")
    @DisplayName("포스트 TestUtil 이용해서 하루 2개 이상의 포스트 쓰기")
    void t001 () throws IOException {
        Member u1 = memberService.getByLoginId("user1");
        Challenge c = challengeService.getChallengeById(2);
        int current = challengePostService.getChallengePostByChallenge(c).size();
        List<ChallengePost> cps = challengePostService.getRecentPosts(c, u1);
        TestUtil.setFieldValue(cps.get(0), "createDate", LocalDateTime.now().minusDays(10));
        ChallengePost post = challengePostService.write("test","test",true, 2,c.getId(),
                "https://kr.object.ncloudstorage.com/devchall/devchall_img/example1.png", u1).getData();
        assertThat(challengePostService.getChallengePostByChallenge(c).size())
                .isEqualTo(current+1);
    }

    @Test
    @DisplayName("하루에 한개 이상 post 작성 제한")
    void t002() throws IOException {
        Member u1 = memberService.getByLoginId("user1");
        Challenge c = challengeService.getChallengeById(2);

        RsData<ChallengePost> postRsData = challengePostService.write("test","test",true, 2,c.getId(),
                "https://kr.object.ncloudstorage.com/devchall/devchall_img/example1.png", u1);
        assertThat(postRsData.isFail()).isTrue();
    }

    @Test
    @DisplayName("totalPost 는 challenge 의 Frequency * week 을 넘지 못한다 : week 2")
    void t003() throws IOException {

        Member u1 = memberService.getByLoginId("admin");
        Challenge c = challengeService.getChallengeById(5);
        TestUtil.setFieldValue(c, "startDate", LocalDate.now().minusDays(12));
        List<ChallengePost> cps = new ArrayList<>();
        
        for(int i=0; i<3; i++){
            cps = challengePostService.getRecentPosts(c, u1);

            if(cps.size()>0)
                TestUtil.setFieldValue(cps.get(0), "createDate", LocalDateTime.now().minusDays(10-i));

            challengePostService.write("test","test",true, 2,
                    c.getId(), "https://kr.object.ncloudstorage.com/devchall/devchall_img/example1.png", u1);
        }


        ChallengeMember challengeMember = challengeMemberService.getByChallengeAndMember(c, u1).get();

        assertThat(challengePostService.getRecentPosts(c, u1).size()).isEqualTo(3);

        assertThat(challengeMemberService.getByChallengeAndMember(c, u1).orElse(null).getTotalPostCount())
                .isEqualTo(c.getChallengePeriod()*c.getChallengeFrequency());
    }

    @Test
    @DisplayName("챌린지에 참여해야 포스트를 작성할 수 있다.")
    void t004() throws IOException {

        Member user4 = memberService.getByLoginId("user4");
        Member user5 = memberService.getByLoginId("user5");
        Member user6 = memberService.getByLoginId("user6");


        String photoUrl = "https://kr.object.ncloudstorage.com/devchall/devchall_img/example1.png";

        Challenge challenge1 = challengeService.createChallengeForNoPhoto("포스트 테스트 챌린지 1", "포스트 테스트를 위한 챌린지 입니다.", true,
                2, LocalDate.parse("2023-08-20"), 1, "C", "개념 공부", "Github", user4);

        challengeMemberService.addMember(challenge1, user5, Role.CREW);

        //user4는 챌린지를 생성한 사람이므로 글쓰기 가능
        RsData<ChallengePost> testRsData1 = challengePostService.write("테스트 인증글1", "테스트 인증글 1번 입니다.",
                true, 3, challenge1.getId(), photoUrl, user4);

        //user5는 챌린지에 참여한 사람이므로 글쓰기 가능
        RsData<ChallengePost> testRsData2 = challengePostService.write("테스트 인증글2", "테스트 인증글 2번 입니다.",
                true, 3, challenge1.getId(), photoUrl, user5);

        //user6은 챌린지에 참여하지 않았으므로 글쓰기 불가
        RsData<ChallengePost> testRsData3 = challengePostService.write("테스트 인증글2", "테스트 인증글 2번 입니다.",
                true, 3, challenge1.getId(), photoUrl, user6);

        assertThat(testRsData1.isSuccess()).isTrue();
        assertThat(testRsData2.isSuccess()).isTrue();
        assertThat(testRsData3.getResultCode().equals("F-1")).isTrue();      //F-1, 챌린지 참여 여부를 확인할 수 없습니다.
    }

    @Test
    @DisplayName("포스트 제목은 최대 24자, 포스트 최대 내용은 500자 이다.")
    void t005() throws IOException {

        Member user4 = memberService.getByLoginId("user4");


        String photoUrl = "https://kr.object.ncloudstorage.com/devchall/devchall_img/example1.png";

        Challenge challenge1 = challengeService.createChallengeForNoPhoto("포스트 테스트 챌린지 1", "포스트 테스트를 위한 챌린지 입니다.", true,
                2, LocalDate.parse("2023-08-20"), 1, "C", "개념 공부", "Github", user4);


        RsData<ChallengePost> testRsData1 = challengePostService.write("테스트 인증글1 인데 제목을 길게 작성하면 생성이 되지 않을지도 몰라요", "테스트 인증글 1번 입니다.",
                true, 3, challenge1.getId(), photoUrl, user4);

        RsData<ChallengePost> testRsData2 = challengePostService.write("테스트 인증글2", "테스트 인증글 1번 입니다.".repeat(50),
                true, 3, challenge1.getId(), photoUrl, user4);

        RsData<ChallengePost> testRsData3 = challengePostService.write("적당한 제목의 테스트 인증글 3", "적당한 길이의 인증글 길이는 500자 미만입니다.",
                true, 3, challenge1.getId(), photoUrl, user4);

        assertThat(testRsData1.getResultCode().equals("F-3")).isTrue();
        assertThat(testRsData2.getResultCode().equals("F-4")).isTrue();
        assertThat(testRsData3.isSuccess()).isTrue();
    }

}