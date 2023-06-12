package com.challenge.devchall.comment.controller;

import com.challenge.devchall.base.rq.Rq;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import com.challenge.devchall.comment.entity.Comment;
import com.challenge.devchall.comment.service.CommentService;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RequestMapping("usr/challenge/comment")
@Controller
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;
    private final Rq rq;
    private final ChallengePostService challengePostService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write/{id}")
    public String createComment(@PathVariable("id") Long id,
                                @RequestParam String contents
    )throws IOException {
        ChallengePost challengePost = challengePostService.getChallengePostById(id);

        Member member = rq.getMember();
        Comment comment = commentService.write(contents,id,member);

        return "redirect:/usr/challenge/postdetail/{id}";

    }

}
