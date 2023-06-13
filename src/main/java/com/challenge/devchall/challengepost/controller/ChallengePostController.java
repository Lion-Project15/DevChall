package com.challenge.devchall.challengepost.controller;


import com.challenge.devchall.base.rq.Rq;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import com.challenge.devchall.comment.service.CommentService;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import com.challenge.devchall.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/challenge")
public class ChallengePostController {

    private final ChallengePostService challengePostService;
    private final ChallengeService challengeService;
    private final ChallengeMemberService challengeMemberService;
    private final MemberService memberService;
    private final PhotoService photoService;
    private final Rq rq;
    private final CommentService commentService;


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
                                  @RequestParam MultipartFile file,
                                  Principal principal,
                                  Model model
    ) throws IOException {

        //포스트를 쓰기 전에, 쓸 수 있는지부터 검사 해야한다.
        Challenge linkedChallenge = challengeService.getChallengeById(id);
        Member member = memberService.findByLoginID(principal.getName()).orElse(null);

        String photoUrl = null;

        if (!file.isEmpty()) {
            photoUrl = photoService.photoUpload(file);
        } else {
            // 이미지 파일이 없는 경우 기본 이미지 URL 설정
            photoUrl = "https://kr.object.ncloudstorage.com/devchall/devchall_img/example1.png";
        }

        ChallengePost post = challengePostService.write(title, contents, status, postScore, id, photoUrl, member).getData();

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
        model.addAttribute("commentList", commentService.findByChallengePost(post));

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

    @GetMapping("/report/{id}")
    public String reportPost(@PathVariable("id") long id, Principal principal) {

        ChallengePost challengePostById = challengePostService.getChallengePostById(id);

        Long linkedChallengeId = challengePostById.getLinkedChallenge().getId();

        // 현재 사용자의 로그인 ID를 가져옴
        String loginId = principal.getName();

        // 게시물 작성자의 로그인 ID를 가져옴
        String postCreatorId = challengePostById.getCreatorId();

        if (loginId.equals(postCreatorId)) {
            System.out.println("자신의 글은 신고할 수 없습니다.");
            return "redirect:/usr/challenge/postdetail/{id}";
        }

        challengePostService.incrementCount(id);
        //FIXME 테스트를 위해 1로 해놓음
        if (challengePostById.getReportCount() >= 1) {
            challengePostService.deletePost(id);
            return "redirect:/usr/challenge/detail/{id}".replace("{id}", String.valueOf(linkedChallengeId));
        }


        return "redirect:/usr/challenge/postdetail/{id}";
    }


}
