package com.challenge.devchall.point.service;

import com.challenge.devchall.base.roles.ChallengeMember.Role;
import com.challenge.devchall.challange.dto.SettleChallengeDTO;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.point.entity.Point;
import com.challenge.devchall.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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
        for(SettleChallengeDTO dto:settleChallengeDTOs) {
            ChallengeMember cm = challengeMemberService.getById(dto.getChallengemember_id()).orElse(null);
            Long postPoints = dto.getCount();
            calcPointFromPosts(cm, postPoints);
            //달성률이 90%을 걸러냄
        }
    }
    public void calcPointFromPosts(ChallengeMember cm, Long postPoints){
        if (cm != null) {
            cm.getChallenger().getPoint().addPoint(postPoints);
        }
    }

        private void calcRewardByRole(ChallengeMember cm, long reward){
        long totalReward = Math.round(reward*1.05); // Role.CREW
        if (cm.getChallengerRole() == Role.LEADER) {
            totalReward = Math.round(reward * 1.07);
        }
        cm.getChallenger().getPoint().addPoint(totalReward);
    }
}
