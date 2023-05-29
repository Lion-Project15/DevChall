package com.challenge.devchall.member.controller;

import com.challenge.devchall.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/member")
public class MemberController {
    private MemberService memberService;

    //회원가입
    @GetMapping ("/join")
    public String showJoin(){
        return "/usr/member/join";
    }

    @AllArgsConstructor
    @Getter
    public static class JoinForm {
        @NotBlank
        @Size(min = 4, max = 30)
        private final String loginID;

        @NotBlank
        @Size(min = 4, max = 30)
        private final String password;
    }
    @PostMapping ("/join")
    public String join(@Valid JoinForm joinForm) {
        memberService.join(joinForm.getLoginID(),joinForm.getPassword());
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLogin () {
        return "/usr/member/login";
    }




}

