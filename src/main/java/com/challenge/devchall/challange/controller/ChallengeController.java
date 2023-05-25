package com.challenge.devchall.challange.controller;


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
            @RequestParam String start_date,
            @RequestParam String end_date
    ){

        System.out.println("title = " + title);
        System.out.println("contents = " + contents);
        System.out.println("status = " + status);
        System.out.println("frequency = " + frequency);
        System.out.println("start_date = " + start_date);
        System.out.println("end_date = " + end_date);

        return "redirect:/";
    }


}
