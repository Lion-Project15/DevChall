package com.challenge.devchall.member.controller;

import com.challenge.devchall.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/member")
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @GetMapping ("/join")
    public String showJoin(){
        return "/usr/member/join";
    }

    @AllArgsConstructor
    @Getter
    public static class JoinForm {
        @NotBlank
        @Size(min = 4, max = 15)
        private final String loginID;

        @NotBlank
        @Size(min = 4, max = 15)
        private final String password;

        @NotBlank
        @Size(min = 1, max = 30)
        private final String username;

        @NotBlank
        @Size(min = 4, max = 30)
        private final String email;

        @NotBlank
        @Size(min = 1, max = 30)
        private final String nickname;
    }

    @PostMapping ("/join")
    public String join(@Valid JoinForm joinForm) {
        memberService.join(joinForm.getLoginID(),
                joinForm.getPassword(),
                joinForm.getEmail(),
                joinForm.getNickname(),
                joinForm.getUsername());
        return "redirect:/";
    }
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin () {
        return "/usr/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public String showMe(Model model){
        return "/usr/member/me";
    }




}

