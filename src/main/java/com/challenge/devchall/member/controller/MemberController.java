package com.challenge.devchall.member.controller;

import com.challenge.devchall.base.rq.Rq;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.member.dto.MemberRequestDto;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.MessageUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/member")
public class MemberController {
    private final MemberService memberService;
    final private ChallengeMemberService challengeMemberService;
    private final Rq rq;

    //회원가입
    @GetMapping("/join")
    public String showJoin (MemberRequestDto memberDto, Model model) {
        model.addAttribute("memberDto", memberDto);
        return "/usr/member/join";
    }

//    @AllArgsConstructor
//    @Getter
//    public static class JoinForm {
//        @NotBlank
//        @Size(min = 4, max = 15)
//        private final String loginID;
//
//        @NotBlank
//        @Size(min = 4, max = 15)
//        private final String password;
//
//        @NotBlank
//        @Size(min = 1, max = 30)
//        private final String username;
//
//        @NotBlank
//        @Size(min = 4, max = 30)
//        private final String email;
//
//        @NotBlank
//        @Size(min = 1, max = 30)
//        private final String nickname;
//    }

    @PostMapping("/join")
    public String join (@Valid MemberRequestDto memberDto, BindingResult bindingResult, Model model) {
        RsData<Member> validateRsData = memberService.validateMember(memberDto.getLoginID(), memberDto.getEmail(), memberDto.getNickname());

        if (bindingResult.hasErrors() || validateRsData.isFail() ) {
            if (validateRsData.isFail()){
                switch (validateRsData.getResultCode()){
                    case "F-1" -> model.addAttribute("valid_loginID",validateRsData.getMsg());
                    case "F-2" -> model.addAttribute("valid_nickname",validateRsData.getMsg());
                    case "F-3" -> model.addAttribute("valid_email",validateRsData.getMsg());
                }

            }
            Map<String, String> validatorResult = memberService.validateHandling(bindingResult);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            model.addAttribute("memberDto",memberDto);
            return "usr/member/join";
        }
        RsData<Member> rsData = memberService.join(memberDto.getLoginID(), memberDto.getPassword(), memberDto.getEmail(), memberDto.getNickname(), memberDto.getUsername());


        return "redirect:/usr/member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin () {
        return "/usr/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public String showMe(Model model){
        if(rq.isLogin()){
            model.addAttribute("challengeMembers"
                    , challengeMemberService.getByMember(rq.getMember()));
        }
        return "/usr/member/me";
    }

    @GetMapping("/store")
    public String getStore(Model model, Principal principal) {
        String memberNickname = principal.getName(); // 현재 인증된 사용자의 닉네임
        int memberPoint = memberService.getMemberPoint(memberNickname);

        model.addAttribute("memberNickname", memberNickname);
        model.addAttribute("memberPoint", memberPoint);
        return "/usr/member/store";
    }

    @GetMapping("/store/buy/{buyCode}")
    public String buyItem(@PathVariable("buyCode") String buyCode,
                          Principal principal){

        Member loginMember = memberService.getByLoginId(principal.getName());

        RsData<Member> buyRsData = memberService.buyItem(buyCode, loginMember);

        System.out.println(buyRsData.getMsg());

        return "redirect:/usr/member/store";
    }

}

