package com.challenge.devchall.member.controller;

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

    //회원가입
    @GetMapping("/join")
    public String showJoin() {
        return "/usr/member/join";
    }

    @PostMapping("/join")
    public String showJoin(
            @RequestParam String loginID,
            @RequestParam String nickname,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam("repeatPassword") String repeatPassword
    ) {
        System.out.println("ID = " + loginID);
        System.out.println("Nickname = " + nickname);
        System.out.println("Email = " + email);
        System.out.println("Password = " + password);
        System.out.println("repeatPassword = " + repeatPassword);
        return "redirect:/";
    }
}

