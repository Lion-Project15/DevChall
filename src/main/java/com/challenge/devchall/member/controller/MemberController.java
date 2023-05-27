package com.challenge.devchall.member.controller;

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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/member")
public class MemberController {
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


    @GetMapping("/login")
    public String showLogin () {
        return "/usr/member/login";
    }

    @PostMapping("/login")
    public String showLogin(
            @RequestParam String loginID,
            @RequestParam String password
    ) {
        System.out.println("loginID = " + loginID);
        System.out.println("password = " + password);

        return "redirect:/";
    }


    @PostMapping ("/join")
    public String showJoin(@Valid JoinForm joinForm) {
        return "redirect:/";
    }

}

