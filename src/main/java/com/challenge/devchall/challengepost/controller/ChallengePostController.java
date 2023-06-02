package com.challenge.devchall.challengepost.controller;


import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
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
    public String writeChallengePost(Model model, @PathVariable("id") long id) {

        Challenge challenge = this.challengeService.getChallengeById(id);

        model.addAttribute("challenge", challenge);

        return "/usr/challenge/write_form";
    }

    @PostMapping("/write_form/{id}")
    public String createChallenge(@PathVariable("id") long id,
                                  @RequestParam String title,
                                  @RequestParam String contents,
                                  @RequestParam boolean status,
                                  @RequestParam long postScore,
                                  Model model
    ) {

        ChallengePost post = challengePostService.write(title, contents, status, id);
        Challenge linkedChallenge = post.getLinkedChallenge();

        model.addAttribute("linkedChallenge", linkedChallenge);
        model.addAttribute("post", post);

        return "redirect:/usr/challenge/detail/{id}";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") long id) {

        ChallengePost challengePostById = challengePostService.getChallengePostById(id);

        Long linkedChallengeId = challengePostById.getLinkedChallenge().getId();

        challengePostService.deletePost(id);

        System.out.println("linkedChallengeId = " + linkedChallengeId);

        return "redirect:/usr/challenge/detail/%d".formatted(linkedChallengeId);
    }

    @GetMapping("/postdetail/{id}")
    public String postDetail(@PathVariable long id, Model model) {

        ChallengePost post = this.challengePostService.getChallengePostById(id);

        Challenge linkedChallenge = post.getLinkedChallenge();

        model.addAttribute("post", post);
        model.addAttribute("linkedChallenge", linkedChallenge);

        return "/usr/challenge/postdetail";
    }

    @GetMapping("/modifypost/{id}")
    public String modifyPost(@PathVariable long id, Model model) {

        ChallengePost post = this.challengePostService.getChallengePostById(id);

        Challenge linkedChallenge = post.getLinkedChallenge();

        model.addAttribute("post", post);
        model.addAttribute("linkedChallenge", linkedChallenge);

        return "/usr/challenge/modifypost";
    }

    @PostMapping("/modifypost/{id}")
    public String modifyPost(@PathVariable long id,
                             @RequestParam String title,
                             @RequestParam String contents,
                             @RequestParam boolean status) {

        challengePostService.modifyPost(id, title, contents, status);

        ChallengePost challengePost = challengePostService.getChallengePostById(id);

        return "redirect:/usr/challenge/postdetail/{id}";
    }


}
