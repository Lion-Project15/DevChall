package com.challenge.devchall.comment.controller;

import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.comment.entity.Comment;
import com.challenge.devchall.comment.service.CommentService;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RequestMapping("usr/challenge/comment")
@Controller
@RequiredArgsConstructor
public class CommentController {
    @Autowired
    private final ChallengeService challengeService;
    private final CommentService commentService;
    private final MemberService memberService;

    @GetMapping("/write_commentForm/{id}")
    public String CommentWrite(Model model, @PathVariable("id") Long id, @RequestParam String content){
        Challenge challenge = this.challengeService.getChallengeById(id);

        model.addAttribute("challenge",challenge);

        return "usr/challenge/comment/write_commentForm";
    }

    @PostMapping("/write_commentForm/{id}")
    public String createComment(@PathVariable("id") Long id,
                                @RequestParam String contents,
                                Principal principal,
                                Model model
    )throws IOException {
        Challenge linkedChallenge = challengeService.getChallengeById(id);
        Member member = memberService.findByLoginID(principal.getName()).orElse(null);

        Comment comment = commentService.write(contents,id,member);

        model.addAttribute("linkedChallenge",linkedChallenge);
        model.addAttribute("comment", comment);

        return "redirect:/usr/challenge/detail/{id}";

    }


}
