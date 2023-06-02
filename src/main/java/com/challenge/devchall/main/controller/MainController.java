package com.challenge.devchall.main.controller;

import com.challenge.devchall.base.rq.Rq;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    final private ChallengeService challengeService;
    final private ChallengeMemberService challengeMemberService;
    private final Rq rq;
    @GetMapping("/")
    public String showMain(Model model){
        if(rq.isLogin()){
            model.addAttribute("challengeMembers"
                    , challengeMemberService.getByMember(rq.getMember()));
        }
        model.addAttribute("challenges",challengeService.getChallengList());
        return "index";
    }

}
