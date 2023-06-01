package com.challenge.devchall.member.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping ("/join")
    public String join(@Valid MemberRequestDto memberDto, BindingResult bindingResult, Model model) {
        /* 검증 */
        if (bindingResult.hasErrors()) {
            /* 회원가입 실패 시 입력 데이터 값 유지 */
            Map<String, String> validatorResult = memberService.validateHandling(bindingResult);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "/usr/member/join";
        }
        memberService.join(memberDto.getLoginID(), memberDto.getPassword(), memberDto.getEmail(), memberDto.getNickname(), memberDto.getUsername());
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
        return "/usr/member/me";
    }
}

