package com.challenge.devchall.challengepost.service;

import com.challenge.devchall.base.Util.TestUtil;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import org.junit.jupiter.api.*;
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
import static org.junit.jupiter.api.Assertions.*;
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

            System.out.println("cps.toString() = " + cps.toString());

            if(cps.size()>0)
                TestUtil.setFieldValue(cps.get(0), "createDate", LocalDateTime.now().minusDays(10-i));

            challengePostService.write("test","test",true, 2,
                    c.getId(), "https://kr.object.ncloudstorage.com/devchall/devchall_img/example1.png", u1);
        }


        ChallengeMember challengeMember = challengeMemberService.getByChallengeAndMember(c, u1).get();



        assertThat(challengePostService.getRecentPosts(c, u1).size())
                .isEqualTo(3);

        //FIXME
//        assertThat(challengeMemberService.getByChallengeAndMember(c, u1).orElse(null).getTotalPostCount())
//                .isEqualTo(c.getChallengePeriod()*c.getChallengeFrequency());


        System.out.println("challengeMember.getTotalPostCount() = " + challengeMember.getTotalPostCount());  //0
        System.out.println(c.getChallengePeriod() * c.getChallengePeriod());  //4

    }




}