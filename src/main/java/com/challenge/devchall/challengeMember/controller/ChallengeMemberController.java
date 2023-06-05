package com.challenge.devchall.challengeMember.controller;

import com.challenge.devchall.base.roles.ChallengeMember.Role;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.repository.ChallengeMemberRepository;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/challenge")
public class ChallengeMemberController {

    private final MemberService memberService;
    private final ChallengeService challengeService;
    private final ChallengeMemberService challengeMemberService;
    private final ChallengeMemberRepository challengeMemberRepository;

    @GetMapping("/join/{id}")
    public String memberJoin(@PathVariable("id") long challengeId, Principal principal){

        //내가 어떤 챌린지에 들어가야되는지도 알고 있고,
        //내가 어떤 사람인지도 알고 있다.
        //일단 참여 안한 상태인 것은 확정임.
        //이제 무조건 추가만 하면 됨. "CREW"

        //내가 들어가야할 챌린지
        Challenge challengeById = challengeService.getChallengeById(challengeId);

        Member loginMember = memberService.findByLoginID(principal.getName()).orElse(null);

        int joinCost = challengeById.getChallengePeriod() * 50;

        RsData<Member> joinRsData = memberService.canJoin(loginMember, joinCost);

        if(joinRsData.isFail()){
            System.out.println(joinRsData.getMsg());
            return "redirect:/usr/challenge/detail/{id}";
        }

        ChallengeMember challengeMember = challengeMemberService.addMember(challengeById, loginMember, Role.CREW);

        return "redirect:/usr/challenge/detail/{id}";
    }
}
