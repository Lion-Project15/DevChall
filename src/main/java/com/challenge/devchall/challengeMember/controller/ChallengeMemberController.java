package com.challenge.devchall.challengeMember.controller;

import com.challenge.devchall.base.rq.Rq;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.role.Role;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/challenge")
public class ChallengeMemberController {

    private final ChallengeService challengeService;
    private final ChallengeMemberService challengeMemberService;
    private final Rq rq;

    @GetMapping("/join/{id}")
    public String memberJoin(@PathVariable("id") long challengeId){

        //내가 들어가야할 챌린지
        Challenge challengeById = challengeService.getChallengeById(challengeId);
        Member loginMember = rq.getMember();
        RsData<ChallengeMember> joinRsData = challengeMemberService.addMember(challengeById, loginMember, Role.CREW);

        if(joinRsData.isFail()){
            System.out.println(joinRsData.getMsg());
            return "redirect:/usr/challenge/detail/{id}";
        }

        return "redirect:/usr/challenge/detail/{id}";
    }
}
