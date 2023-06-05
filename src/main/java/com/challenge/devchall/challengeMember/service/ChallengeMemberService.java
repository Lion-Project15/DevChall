package com.challenge.devchall.challengeMember.service;

import com.challenge.devchall.base.roles.ChallengeMember.Role;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.repository.ChallengeMemberRepository;
import com.challenge.devchall.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
