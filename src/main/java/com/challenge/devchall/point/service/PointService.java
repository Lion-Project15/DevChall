package com.challenge.devchall.point.service;

import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.role.Role;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.dto.SettleChallengeDTO;
import com.challenge.devchall.point.entity.Point;
import com.challenge.devchall.point.repository.PointRepository;
import com.challenge.devchall.pointHistory.repository.PointHistoryRepository;
import com.challenge.devchall.pointHistory.service.PointHistoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final ChallengeMemberService challengeMemberService;
    private final PointHistoryService pointHistoryService;
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
        Map<Long, CompletedChallenge> completedMap = new HashMap<>();
        long reward = 1;

        for(SettleChallengeDTO dto:settleChallengeDTOs) {
            if (dto== null){
                break;
            }

            ChallengeMember cm = challengeMemberService.getById(dto.getChallengemember_id()).orElse(null);

            if (cm==null){
                continue;
            }

            //정산 완료 표시
            Challenge challenge = cm.getLinkedChallenge();

            if(!challenge.isSettleComplete()){
                challenge.complete();
            }

            Long postPoints = dto.getCount();

            //Post 개수 만큼 포인트 증가
            calcPointFromPosts(cm, postPoints);

            //달성률이 90%을 map: list에 넣음
            if(checkAchievementRate(cm, challenge)){
                if(completedMap.containsKey(challenge.getId())){ //챌린지 별로 맵에 넣음
                    completedMap.get(challenge.getId()).getRecipients().add(cm);
                }
                else{
                    completedMap.put(challenge.getId(), new CompletedChallenge(){{
                                        getRecipients().add(cm);
                                        setGatherPoint(challenge.getGatherPoints());}}
                    );
                }
            }
        }
        for(long challengeId: completedMap.keySet()){
            CompletedChallenge cc = completedMap.get(challengeId);
            if(cc.getRecipients().size() > 0) {
                reward = (long)(Math.round(cc.getGatherPoint()) / (double) cc.getRecipients().size());
                for (ChallengeMember cm : cc.getRecipients()) {
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

    private void calcReward(){

    }

    private void calcRewardByRole(ChallengeMember cm, long reward){
        long totalReward = Math.round(reward*1.05); // Role.CREW
        if (cm.getChallengerRole() == Role.LEADER) {
            totalReward = Math.round(reward * 1.07);
        }
        pointHistoryService.addPointHistory(cm.getChallenger(), totalReward, "챌린지 정산");
        cm.getChallenger().getPoint().add((int) totalReward);
    }

    public boolean checkAchievementRate(ChallengeMember cm, Challenge challenge) {
        if(cm != null && cm.isValid()){

            double achievementRate
                    = ((double) cm.getTotalPostCount())
                        / (challenge.getChallengePeriod() * challenge.getChallengeFrequency()) * 100;
            return achievementRate>=90; //90% 이상
        }
        return false;
    }
}
@Getter
@Setter
class CompletedChallenge{
    private List<ChallengeMember> recipients = new ArrayList<>();
    private long gatherPoint;
}