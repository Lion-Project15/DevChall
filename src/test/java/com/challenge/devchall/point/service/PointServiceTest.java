package com.challenge.devchall.point.service;


import com.challenge.devchall.base.Util.TestUtil;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.role.Role;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import com.challenge.devchall.point.entity.Point;
import com.challenge.devchall.point.repository.PointRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
    private ChallengeService challengeService;
    @Autowired
    private MemberService memberService;
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
    @DisplayName("챌린지1에서 90% 이상의 달성자만 보상")
    void settle01 () {
        //challenge1 -> (post=2): point=100, freq=1, period=2, start=6/1, end=6/15 __ user1,2
        Challenge c = getChallengeDeadlineToday(1);

        List<Member> mem = getMemberlist(c);

        List<Long> points = getPoints(mem);

        writePost(2, c, mem.get(0));//admin
        writePost(1, c, mem.get(1));//user1

        setTotalPost(c, mem);

        pointService.settle();

        int memberNum = howManyEarn(mem, c);

        for(int i = 0; i<mem.size(); i++){
            assertThat(mem.get(i).getPoint().getCurrentPoint()).isEqualTo(points.get(i)
                    + calcReward(c, mem.get(i), memberNum) );
        }
    }

    @Test
    @DisplayName("챌린지2에서 90% 이상의 달성자만 보상")
    void settle02 () {
        //challenge2 -> (post=12): point=800, freq=3, period=4, start=6/1, end=6/29 __a,user1,2,3
        Challenge c = getChallengeDeadlineToday(2);

        List<Member> mem = getMemberlist(c);

        List<Long> points = getPoints(mem);

        writePost(11, c, mem.get(0));//admin
        writePost(10, c, mem.get(1));//user1
        writePost(9, c, mem.get(2));//user2

        setTotalPost(c, mem);

        pointService.settle();

        int memberNum = howManyEarn(mem, c);

        for(int i = 0; i<mem.size(); i++){
            assertThat(mem.get(i).getPoint().getCurrentPoint()).isEqualTo(points.get(i)
                    + calcReward(c, mem.get(i), memberNum));
        }
    }

    @Test
    @DisplayName("챌린지3에서 90% 이상의 달성자만 보상")
    void settle03 () {
        //challenge3 -> (post=12): point=1200, freq=7, period=8, start=6/1, end=7/20 __a,user2,4,5
        Challenge c = getChallengeDeadlineToday(3);

        List<Member> mem = getMemberlist(c);

        List<Long> points = getPoints(mem);

        writePost(56, c, mem.get(0));//user2
        writePost(51, c, mem.get(1));//user4
        writePost(50, c, mem.get(2));//user5

        setTotalPost(c, mem);

        pointService.settle();

        int memberNum = howManyEarn(mem, c);

        for(int i = 0; i<mem.size(); i++){
            assertThat(mem.get(i).getPoint().getCurrentPoint()).isEqualTo(points.get(i)
                    + calcReward(c, mem.get(i), memberNum));
        }
    }

    @Test
    @DisplayName("챌린지1과 챌린지2 동시 처리")
    void settle04 () {
        //challenge1 -> (post=2): point=100, freq=1, period=2, start=6/1, end=6/15 __ a, user1,2
        Challenge c1 = getChallengeDeadlineToday(1);
        //challenge2 -> (post=12): point=800, freq=3, period=4, start=6/1, end=6/29 __a,user1,2,3
        Challenge c2 = getChallengeDeadlineToday(2);

        List<Member> mem1 = getMemberlist(c1);
        List<Member> mem2 = getMemberlist(c2);


//        List<Long> points1 = getPoints(mem1);
//        List<Long> points2= getPoints(mem2);
        List<Long> points = getPoints(allMemberList());

        writePost(2, c1, mem1.get(0));//admin
        writePost(1, c1, mem1.get(1));//user1

        writePost(11, c2, mem2.get(0));//admin
        writePost(10, c2, mem2.get(1));//user1
        writePost(9, c2, mem2.get(2));//user2

        setTotalPost(c1, mem1);
        setTotalPost(c2, mem2);

        pointService.settle();

        int memberNum1 = howManyEarn(mem1, c1);
        int memberNum2 = howManyEarn(mem2, c2);

        List<Member> allMem = allMemberList();
        for(int i = 0; i<allMem.size(); i++){
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+allMem.get(i).getLoginID());
            assertThat(allMem.get(i).getPoint().getCurrentPoint()).isEqualTo(
                    points.get(i)
                    + calcReward(c1, allMem.get(i), memberNum1)
                    + calcReward(c2, allMem.get(i), memberNum2));
        }
    }

    private Challenge getChallengeDeadlineToday(long cId){
        Challenge c = challengeService.getChallengeById(cId);
        TestUtil.setFieldValue(c, "endDate", LocalDate.now());
        return c;
    }

    private List<Member> allMemberList(){
        return new ArrayList<>(){{
            add(memberService.getByLoginId("admin"));
            add(memberService.getByLoginId("user1"));
            add(memberService.getByLoginId("user2"));
            add(memberService.getByLoginId("user3"));
            add(memberService.getByLoginId("user4"));
            add(memberService.getByLoginId("user5"));
        }};
    }
    private List<Member> getMemberlist(Challenge c){
        List<Member> mem = new ArrayList<>(){{
            add(memberService.getByLoginId("admin"));
            add(memberService.getByLoginId("user1"));
            add(memberService.getByLoginId("user2"));
            add(memberService.getByLoginId("user3"));
            add(memberService.getByLoginId("user4"));
            add(memberService.getByLoginId("user5"));
        }};
        mem.removeIf(m -> !challengeMemberService.getByChallengeAndMember(c, m).isPresent());
        return mem;
    }

    private List<Long> getPoints(List<Member> mem){
        return new ArrayList<>(){{
            for(Member m: mem){
                add(m.getPoint().getCurrentPoint());
            }
        }};
    }


    private void writePost(int n, Challenge c, Member m){
        for(int i=0; i<n; i++){
            List<ChallengePost> posts = challengePostService.getRecentPosts(c, m);
            if(posts.size()>0){
                TestUtil.setFieldValue(posts.get(0), "createDate", LocalDateTime.now().minusDays(4));
            }
            //포스트 쓰기
            challengePostService.write("test","test",true, 2, c.getId(),
                    "https://kr.object.ncloudstorage.com/devchall/devchall_img/example1.png", m).getData();

        }
    }

    private Long calcReward(Challenge c, Member m, int memberNum) {
        long postPoint = challengePostService.getRecentPosts(c, m).size();
        ChallengeMember cm = challengeMemberService.getByChallengeAndMember(c,m).orElse(null);
        if(cm==null || memberNum == 0){
            return 0L;
        }
        long reward = c.getGatherPoints()/memberNum;
        long totalReward = 0;
        if(pointService.checkAchievementRate(cm, c)){
            totalReward = cm.getChallengerRole() == Role.LEADER ?
                    Math.round(reward * 1.07): Math.round(reward*1.05);
        }


        return totalReward+postPoint;
    }

    private int howManyEarn(List<Member> mem, Challenge c){
        mem.removeIf(m->!pointService.checkAchievementRate(
                challengeMemberService.getByChallengeAndMember(c,m).orElse(null), c));
        return mem.size();
    }

    private void setTotalPost(Challenge c, List<Member> mem){
        for(Member m: mem){
            ChallengeMember cm = challengeMemberService.getByChallengeAndMember(c,m).orElse(null);
            int total = challengePostService.getRecentPosts(c, m).size();
            if(total>c.getChallengeFrequency()*c.getChallengePeriod()){
                total = c.getChallengeFrequency()*c.getChallengePeriod();
            }
            TestUtil.setFieldValue(cm, "totalPostCount", total);
        }
    }

}