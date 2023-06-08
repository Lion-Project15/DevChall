package com.challenge.devchall.main.controller;

import com.challenge.devchall.base.rq.Rq;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.dto.SettleChallengeDTO;
import com.challenge.devchall.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    final private ChallengeService challengeService;
    final private ChallengeMemberService challengeMemberService;
    final private PointService pointService;
    private final Rq rq;
    @GetMapping("/")
    public String showMain(Model model,
                           @RequestParam(required = false, defaultValue = "") String language,
                           @RequestParam(required = false, defaultValue = "") String subject){
        language = language.isBlank()? null: language.trim();
        subject = subject.isBlank()? null : subject.trim();
        if(rq.isLogin()){
            model.addAttribute("challengeMembers"
                    , challengeMemberService.getByMember(rq.getMember()));
            model.addAttribute("challenges",
                    challengeService.getChallengList(language, subject, rq.getMember()));
        } else {
            model.addAttribute("challenges",
                    challengeService.getChallengList(language,subject));
        }
        return "index";
    }

    @GetMapping("/test")
    public String test() {
        pointService.settle();
        return "redirect:/usr/member/me" ;
    }

    @ResponseBody
    @GetMapping("/test2")
    public List<SettleChallengeDTO> test2() {

        return challengeMemberService.getSettleChallengeDto();
    }

}
