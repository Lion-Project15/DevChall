package com.challenge.devchall.point.service;
import com.challenge.devchall.base.roles.ChallengeMember.Role;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.dto.SettleChallengeDTO;
import com.challenge.devchall.point.entity.Point;
import com.challenge.devchall.point.repository.PointRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional()
public class PointService {
    private final PointRepository pointRepository;
    private final ChallengeMemberService challengeMemberService;
    List<SettleChallengeDTO> settleChallengeDTOs;


    public Point create(){
        Point p = pointRepository.save(Point.builder()
                .currentPoint(1000L)
                .totalPoint(1000L)
                .build());

        return pointRepository.findById(p.getId()).orElse(null);
    }

    public void settle(){
        //완료된 챌린지를 리스트로 가져옴
        settleChallengeDTOs = challengeMemberService.getSettleChallengeDto();
        Map<Challenge, List<ChallengeMember>> completedMap = new HashMap<>();
        long reward = 1;

        for(SettleChallengeDTO dto:settleChallengeDTOs) {
            if (dto== null){
                break;
            }

            ChallengeMember cm = challengeMemberService.getById(dto.getChallengemember_id()).orElse(null);
            if (cm==null){
                continue;
            }
            Challenge challenge = cm.getLinkedChallenge();
            Long postPoints = dto.getCount();
            //Post 개수 만큼 포인트 증가
            calcPointFromPosts(cm, postPoints);

            //달성률이 90%을 걸러냄
            if(checkAchievementRate(cm, challenge)){
                if(completedMap.containsKey(challenge)){ //챌린지 별로 맵에 넣음
                    completedMap.get(challenge).add(cm);
                }
                else{
                    completedMap.put(challenge,new ArrayList<>(){{add(cm);}});
                }
            }
        }
        for(Challenge c: completedMap.keySet()){
            if(completedMap.get(c).size() > 0){
                reward = Math.round(c.getGatherPoints()/(double)completedMap.get(c).size());
                for(ChallengeMember cm : completedMap.get(c)){
                    calcRewardByRole(cm, reward);
                }
            }
        }


    }
    public void calcPointFromPosts(ChallengeMember cm, Long postPoints){
        if (cm != null) {
            cm.getChallenger().getPoint().add(postPoints.intValue());
        }
    }

    private void calcRewardByRole(ChallengeMember cm, long reward){
        long totalReward = Math.round(reward*1.05); // Role.CREW
        if (cm.getChallengerRole() == Role.LEADER) {
            totalReward = Math.round(reward * 1.07);
        }
        cm.getChallenger().getPoint().add((int) totalReward);
    }
    public boolean checkAchievementRate(ChallengeMember cm, Challenge challenge) {
        if(cm != null){

            double achievementRate
                    = ((double) cm.getTotalPostCount())
                        / (challenge.getChallengePeriod() * challenge.getChallengeFrequency()) * 100;
            return achievementRate>=90;
        }
        return false;
    }
}
@Getter
@Setter
class CompletedChallenge{
    private List<ChallengeMember> challengeMembers = new ArrayList<>();
    private long reward;
    private long challengeId;
}