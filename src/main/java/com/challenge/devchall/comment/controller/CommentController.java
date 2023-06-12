package com.challenge.devchall.comment.controller;

import com.challenge.devchall.base.rq.Rq;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
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

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequestMapping("/usr/challenge/comment")
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final ChallengeService challengeService;
    private final CommentService commentService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write/{id}")
    public String createComment(@PathVariable("id") Long id,
                                @RequestParam String contents
    )throws IOException {
        Challenge linkedChallenge = challengeService.getChallengeById(id);
        Member member = rq.getMember();

        Comment comment = commentService.write(contents,id,member);

        return "redirect:/usr/challenge/detail/{id}";

    }


}
