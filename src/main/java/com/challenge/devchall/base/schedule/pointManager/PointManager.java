package com.challenge.devchall.base.schedule.pointManager;

import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import com.challenge.devchall.member.entity.Member;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PointManager {
    List<Challenge> doneChallenges;
    private final ChallengePostService challengePostService;
    private final ChallengeMemberService challengeMemberService;
    public void settle(){
        //완료된 챌린지를 리스트로 가져옴

    }
//    public int calcPointFromPosts(Member member){
//        List<ChallengePost> posts= challengePostService.getChallengePostByChallenge();
//
//    }
}
