package com.challenge.devchall.challange.controller;


import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.repository.ChallengeRepository;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.repository.ChallengeMemberRepository;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.repository.MemberRepository;
import com.challenge.devchall.member.service.MemberService;
import com.challenge.devchall.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/challenge")
public class ChallengeController {

    private final ChallengeMemberService challengeMemberService;
    private final MemberService memberService;
    private final ChallengeService challengeService;
    private final PhotoService photoService;
    private final ChallengeRepository challengeRepository;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createChallenge() {

        System.out.println("get mapping");

        return "/usr/challenge/create_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createChallenge(
            @RequestParam String title, @RequestParam String contents,
            @RequestParam boolean status, @RequestParam String frequency,
            @RequestParam String startDate, @RequestParam String period,
            @RequestParam String language, @RequestParam String subject,
            @RequestParam String posttype, @RequestParam MultipartFile file,
            Principal principal
    ) throws IOException {

        RsData<String> imgRsData = isImgFile(file.getOriginalFilename());

        if (!file.isEmpty() && imgRsData.isSuccess()) {
            System.out.println(imgRsData.getMsg());
        }else{
            //toast ui warning으로 처리?
            System.out.println(imgRsData.getMsg());
            return "redirect:/";
        }


        String photoUrl = photoService.photoUpload(file);

        Member loginMember = memberService.getByLoginId(principal.getName());

        challengeService.createChallenge(title, contents, status, frequency, startDate, period,
                language, subject, posttype, photoUrl, loginMember);

        return "redirect:/";
    }

    @GetMapping("/list")
    public String list(Model model) {

        List<Challenge> challengeList = this.challengeRepository.findAll();
        model.addAttribute("challengeList", challengeList);

        //FIXME
        return "/usr/challenge/list";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(Model model, @PathVariable("id") long id, Principal principal) {

        Challenge challenge = this.challengeService.getChallengeById(id);
        Member loginMember = memberService.getByLoginId(principal.getName());

        Optional<ChallengeMember> byChallengeAndMember = challengeMemberService.getByChallengeAndMember(challenge, loginMember);

        boolean isJoin;

        if (byChallengeAndMember.isPresent()) {
            isJoin = true;
        } else {
            isJoin = false;
        }

        boolean hasPost = challengeService.hasPost(challenge);

        if (hasPost) {
            List<ChallengePost> challengePostList = challenge.getChallengePostList();

            model.addAttribute("challengePostList", challengePostList);
        }

        model.addAttribute("challenge", challenge);
        model.addAttribute("hasPost", hasPost);
        model.addAttribute("isJoin", isJoin);

        return "/usr/challenge/detail";
    }

    public RsData<String> isImgFile(String fileName) {

        //확장자 추출
        String fileExtension = StringUtils.getFilenameExtension(fileName);

        if (fileExtension != null && (fileExtension.equals("jpg") || fileExtension.equals("jpeg")
                || fileExtension.equals("png") || fileExtension.equals("gif"))){

            return RsData.of("S-6", "이미지가 맞습니다.");
        }
        else
            return RsData.of("F-6", "이미지만 업로드가 가능합니다.");

    }

}
