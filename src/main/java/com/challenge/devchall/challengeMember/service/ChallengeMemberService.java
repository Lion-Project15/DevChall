package com.challenge.devchall.challengeMember.service;


import com.challenge.devchall.base.config.AppConfig;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.repository.ChallengeMemberRepository;
import com.challenge.devchall.challengeMember.role.Role;
import com.challenge.devchall.challengepost.dto.SettleChallengeDTO;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.pointHistory.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeMemberService {

    private final ChallengeMemberRepository challengeMemberRepository;
    private final PointHistoryService pointHistoryService;

    public RsData<ChallengeMember> addMember(Challenge challenge, Member member, Role role){
        if(challenge == null){
            return RsData.of("F-1","챌린지가 존재하지 않습니다.");
        }
        long joinCost = (long) challenge.getChallengePeriod() * AppConfig.getWeeklyPoint();
        RsData<ChallengeMember> joinRsData = canJoin(member, challenge);

        if(joinRsData.isFail()){
            System.out.println(joinRsData.getMsg());
            return joinRsData;
        }

        ChallengeMember challengeMember = ChallengeMember
                .builder()
                .linkedChallenge(challenge)
                .challenger(member)
                .isValid(true)
                .challengerRole(role)
                .totalPostCount(0)
                .build();

        joinRsData.setData(challengeMemberRepository.save(challengeMember));
        member.getPoint().subtract(joinCost);
        challenge.addPoint(joinCost);
        pointHistoryService.addPointHistory(member, -joinCost, "참가비");

        return joinRsData;
    }

    public Optional<ChallengeMember> getByChallengeAndMember(Challenge challenge, Member member){

        return challengeMemberRepository.findByLinkedChallengeAndChallenger(challenge, member);
    }

    public List<ChallengeMember> getByChallenge(Challenge challenge) {
        return challengeMemberRepository.findByLinkedChallenge(challenge);
    }

    public List<ChallengeMember> getByMember(Member member){
        return challengeMemberRepository.findByChallenger(member);
    }

    private RsData<ChallengeMember> canJoin(Member member, Challenge challenge){
        long joinCost = (long) challenge.getChallengePeriod() * AppConfig.getWeeklyPoint();

        Long currentPoint = member.getPoint().getCurrentPoint();

        if(!challenge.getStartDate().isAfter(LocalDate.now())){ //챌린지 시작 전인가?
            return RsData.of("F-4", "챌린지가 이미 시작했습니다.");
        }

        int joinMember = challenge.getChallengeMemberLimit()
                - getChallengeUserCount(challenge);

        if(joinMember<=0){
            return RsData.of("F-5", "챌린지의 인원이 모두 찼습니다.");
        }

        if(currentPoint >= joinCost){
            return RsData.of("S-1", "챌린지 가입을 할 수 있습니다");
        }else{
            return RsData.of("F-3", "참가 비용이 부족합니다.");
        }
    }

    public List<SettleChallengeDTO> getSettleChallengeDto() {
        return challengeMemberRepository.findChallengeMemberCountByEndDate(LocalDate.now());
    }

    public int getCountByChallengeAndMember(Challenge challenge, Member member){

        Optional<ChallengeMember> challengeMember = challengeMemberRepository.findByLinkedChallengeAndChallenger(challenge, member);

        if(challengeMember.isPresent()){
            ChallengeMember myChallengeMember = challengeMember.get();
            return myChallengeMember.getTotalPostCount();
        }else{
            return 0;
        }
    }

    public int getChallengeUserCount(Challenge challenge){

        List<ChallengeMember> byLinkedChallenge = challengeMemberRepository.findByLinkedChallenge(challenge);

        return byLinkedChallenge.size();
    }

    public Optional<ChallengeMember> getById(long id){
        return challengeMemberRepository.findById(id);
    }

}
