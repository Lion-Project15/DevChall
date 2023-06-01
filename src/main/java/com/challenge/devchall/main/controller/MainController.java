package com.challenge.devchall.main.controller;

import com.challenge.devchall.challange.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    final private ChallengeService challengeService;
    @GetMapping("/")
    public String showMain(Model model){
        model.addAttribute("challenges",challengeService.getChallengList());
        return "index";
    }

}
