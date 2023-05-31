package com.challenge.devchall.challange.controller;


import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.repository.ChallengeRepository;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.repository.ChallengeMemberRepository;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;
    private final ChallengeMemberRepository challengeMemberRepository;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createChallenge(){

        System.out.println("get mapping");

        return "/usr/challenge/create_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createChallenge(
            @RequestParam String title,
            @RequestParam String contents,
            @RequestParam String status,
            @RequestParam String frequency,
            @RequestParam String startDate,
            @RequestParam String endDate,
            Principal principal
    ){

        Member loginMember = memberRepository.findByLoginID(principal.getName()).orElse(null);

        challengeService.createChallenge(title, contents, status, frequency, startDate, endDate, loginMember);

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
    public String showDetail(Model model, @PathVariable("id") long id, Principal principal){

        String name = principal.getName();
        Member member = memberRepository.findByUsername(name).orElse(null);

        Challenge challenge = this.challengeService.getChallengeById(id);
        boolean hasPost = challengeService.hasPost(challenge);

        Optional<ChallengeMember> byChallenger = challengeMemberRepository.findByChallenger(member);

        if(hasPost){
            List<ChallengePost> challengePostList = challenge.getChallengePostList();

            model.addAttribute("challengePostList", challengePostList);
        }

        model.addAttribute("challenge", challenge);
        model.addAttribute("hasPost", hasPost);

        return "/usr/challenge/detail";
    }

}
