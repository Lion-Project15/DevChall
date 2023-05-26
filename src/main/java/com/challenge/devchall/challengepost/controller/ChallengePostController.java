package com.challenge.devchall.challengepost.controller;


import com.challenge.devchall.challengepost.service.ChallengePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/challenge")
public class ChallengePostController {

    private final ChallengePostService challengePostService;

    @GetMapping("/write")
    public String writeChallengePost(){

        System.out.println("post write get mapping");

        return "/usr/challenge/write_form";
    }

    @PostMapping("/write")
    public String createChallenge(
            @RequestParam String title,
            @RequestParam String contents,
            @RequestParam String status
    ){

        challengePostService.write(title, contents, status);

        //FIXME 하드코딩 되어있음. 작성한 챌린지로 넘어가야함
        return "redirect:/usr/challenge/detail/1";
    }


}
