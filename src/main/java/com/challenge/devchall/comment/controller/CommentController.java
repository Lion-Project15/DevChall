package com.challenge.devchall.comment.controller;

import com.challenge.devchall.base.rq.Rq;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import com.challenge.devchall.comment.entity.Comment;
import com.challenge.devchall.comment.service.CommentService;
import com.challenge.devchall.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

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
                                @RequestParam String contents,
                                Model model
    )throws IOException {
        ChallengePost challengePost = challengePostService.getChallengePostById(id);

        Member member = rq.getMember();

        RsData<Comment> commentRsData = commentService.write(contents, id, member);
        if (commentRsData.getResultCode().equals("F-1")){
            return rq.historyBack("댓글은 챌린지에 참가한 맴버만 작성 할 수 있습니다.");
        }

        return "redirect:/usr/challenge/postdetail/{id}";

    }

}
