package com.challenge.devchall.challengeMember.service;

import com.challenge.devchall.base.roles.ChallengeMember.Role;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.dto.SettleChallengeDTO;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.repository.ChallengeMemberRepository;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.point.entity.Point;
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


    public ChallengeMember addMember(Challenge challenge, Member member, Role role){

        ChallengeMember challengeMember = ChallengeMember
                .builder()
                .linkedChallenge(challenge)
                .challenger(member)
                .isValid(true)
                .challengerRole(role)
                .postLimit(0)
                .build();

        challengeMemberRepository.save(challengeMember);

        return challengeMember;
    }
    public Optional<ChallengeMember> getById(long id){
        return challengeMemberRepository.findById(id);
    }
    public Optional<ChallengeMember> getByChallengeAndMember(Challenge challenge, Member member){

        return challengeMemberRepository.findByLinkedChallengeAndChallenger(challenge, member);
    }

    public List<ChallengeMember> getByMember(Member member){
        return challengeMemberRepository.findByChallenger(member);
    }

    public List<ChallengeMember> getByChallenge(Challenge challenge) {
        return challengeMemberRepository.findByLinkedChallenge(challenge);
    }
    public RsData<ChallengeMember> canJoin(Member member, long joinCost){

        Point memberPoint = member.getPoint();
        Long currentPoint = memberPoint.getCurrentPoint();

        if(currentPoint >= joinCost){
            memberPoint.subtractPoint((joinCost));
            return RsData.of("S-1", "참가 비용을 지불할 수 있습니다");
        }else{
            return RsData.of("F-3", "참가 비용이 부족합니다.");
        }
    }

    public List<Long> getChallengeIdsByMember(Member member) {
        List<ChallengeMember> challengeMembers = getByMember(member);
        List<Long> challengeIds = new ArrayList<>();

        for (ChallengeMember challengeMember : challengeMembers) {
            Challenge challenge = challengeMember.getLinkedChallenge();
            challengeIds.add(challenge.getId());
        }

        return challengeIds;
    }
    public List<SettleChallengeDTO> getSettleChallengeDto(){
//        return challengeRepository.findChallengeMemberCountByEndDate(LocalDate.now());
        return challengeMemberRepository.findChallengeMemberCountByEndDate(LocalDate.of(2023, 6, 29));
        //    public List<Challenge> getChallengList() {
        //        Sort sort = Sort.by(Sort.Direction.ASC, "createDate");
        //        Pageable pageable = PageRequest.of(0, 30, sort);
        //        List<Challenge> challenges = challengeRepository.findByChallengeStatus(true, pageable);
        //        return challenges;
        //    }
    }
}
