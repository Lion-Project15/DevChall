package com.challenge.devchall.challange.controller;


import com.challenge.devchall.challange.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;


@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/create")
    public String createChallenge(){

        System.out.println("get mapping");

        return "/usr/challenge/create_form";
    }

    @PostMapping("/create")
    public String createChallenge(
            @RequestParam String title,
            @RequestParam String contents,
            @RequestParam String status,
            @RequestParam String frequency,
            @RequestParam String startDate,
            @RequestParam String endDate
    ){

        challengeService.createChallenge(title, contents, status, frequency, startDate, endDate);

        return "redirect:/";
    }


}
