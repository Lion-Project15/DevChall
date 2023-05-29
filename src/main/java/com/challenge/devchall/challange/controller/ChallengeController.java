package com.challenge.devchall.challange.controller;


import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.repository.ChallengeRepository;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/challenge")
@PreAuthorize("isAuthenticated()")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final ChallengeRepository challengeRepository;


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
    @GetMapping("/list")
    public String list(Model model){

        List<Challenge> challengeList = this.challengeRepository.findAll();
        model.addAttribute("challengeList", challengeList);

        //FIXME
        return "/usr/challenge/list";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(Model model, @PathVariable("id") long id){

        Challenge challenge = this.challengeService.getChallengeById(id);
        boolean hasPost = challengeService.hasPost(challenge);
        
        if(hasPost){
            List<ChallengePost> challengePostList = challenge.getChallengePostList();

            model.addAttribute("challengePostList", challengePostList);
        }

        model.addAttribute("challenge", challenge);
        model.addAttribute("hasPost", hasPost);

        return "/usr/challenge/detail";
    }


}
