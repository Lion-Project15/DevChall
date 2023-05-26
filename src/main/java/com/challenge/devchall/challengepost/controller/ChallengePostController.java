package com.challenge.devchall.challengepost.controller;


import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/challenge")
public class ChallengePostController {

    private final ChallengePostService challengePostService;
    private final ChallengeService challengeService;


    @GetMapping("/write_form/{id}")
    public String writeChallengePost(Model model, @PathVariable("id") long id){

        Challenge challenge = this.challengeService.getChallengeById(id);

        model.addAttribute("challenge", challenge);

        return "/usr/challenge/write_form";
    }

    @PostMapping("/write_form/{id}")
    public String createChallenge(@PathVariable("id") long id,
            @RequestParam String title,
            @RequestParam String contents,
            @RequestParam String status
    ){

        challengePostService.write(title, contents, status, id);

        //FIXME 하드코딩 되어있음. 작성한 챌린지로 넘어가야함
        return "redirect:/usr/challenge/detail/1";
    }


}
