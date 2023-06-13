package com.challenge.devchall.challange.controller;


import com.challenge.devchall.base.rq.Rq;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import com.challenge.devchall.comment.entity.Comment;
import com.challenge.devchall.comment.service.CommentService;
import com.challenge.devchall.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/challenge")
public class ChallengeController {

    private final ChallengeMemberService challengeMemberService;
    private final ChallengeService challengeService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createChallenge() {

        if (rq.isLogout()) {
            return rq.historyBack(RsData.of("F-1", "챌린지를 생성하려면 로그인이 필요합니다."));
        }

        return "/usr/challenge/create_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createChallenge(
            @RequestParam String title, @RequestParam String contents,
            @RequestParam boolean status, @RequestParam int frequency,
            @RequestParam String startDate, @RequestParam int period,
            @RequestParam String language, @RequestParam String subject,
            @RequestParam String postType, @RequestParam(required = false) MultipartFile file
    ) throws IOException {

        if (rq.isLogout()) {
            return rq.historyBack(RsData.of("F-1", "챌린지를 생성하려면 로그인이 필요합니다."));
        } else {

            Member loginMember = rq.getMember();

            RsData<Challenge> createRsData = challengeService.createChallenge(title, contents, status, frequency,
                    startDate, period, language, subject, postType, file, loginMember);

            if (createRsData.isFail()) {
                //FIXME 실패시 메세지가 뜨지 않는 상황
                return rq.historyBack(createRsData);
            }

            return rq.redirectWithMsg("/", createRsData);
        }
    }

    @GetMapping("/detail/{id}")
    public String showDetail(Model model, @PathVariable("id") long id) {

        Challenge challenge = this.challengeService.getChallengeById(id);

        Member loginMember = rq.getMember();

        Optional<ChallengeMember> byChallengeAndMember = challengeMemberService.getByChallengeAndMember(challenge, loginMember);

        List<ChallengePost> challengePostList = challenge.getChallengePostList();

        if(!challengePostList.isEmpty()){
            model.addAttribute("challengePostList", challengePostList);
        }

        model.addAttribute("challenge", challenge);
        model.addAttribute("byChallengeAndMember", byChallengeAndMember);


        return "/usr/challenge/detail";
    }

}
