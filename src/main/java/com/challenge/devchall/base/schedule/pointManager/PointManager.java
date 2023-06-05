package com.challenge.devchall.base.schedule.pointManager;

import com.challenge.devchall.challange.dto.SettleChallengeDTO;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
public class PointManager {
    List<SettleChallengeDTO> settleChallengeDTOs;
    private final ChallengeService challengeService;
    private final ChallengeMemberService challengeMemberService;
    public void settle(){
        //완료된 챌린지를 리스트로 가져옴
        settleChallengeDTOs = challengeService.getSettleChallengeDto();
        calcPointFromPosts();
    }
    public void calcPointFromPosts(){
        for(SettleChallengeDTO dto:settleChallengeDTOs){
            ChallengeMember cm  = challengeMemberService.getById(dto.getChallengemember_id()).orElse(null);
            Long postPoints = dto.getCount();
            if(cm != null){
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+postPoints+"//"+cm.getChallenger().getLoginID()+"//"
                        +cm.getChallenger().getPoint().getCurrentPoint());
                cm.getChallenger().getPoint().addPoint(postPoints);
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>after:"+cm.getChallenger().getPoint().getCurrentPoint());

            }
        }
    }
}
