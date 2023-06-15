package com.challenge.devchall.member.controller;

import com.challenge.devchall.base.rq.Rq;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.inventory.entity.Inventory;
import com.challenge.devchall.item.entity.Item;
import com.challenge.devchall.item.service.ItemService;
import com.challenge.devchall.member.dto.MemberRequestDto;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import com.challenge.devchall.photo.service.PhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/member")
public class MemberController {
    private final MemberService memberService;
    private final ChallengeMemberService challengeMemberService;
    private final ItemService itemService;
    private final Rq rq;
    private final PhotoService photoService;

    //회원가입
    @GetMapping("/join")
    public String showJoin (MemberRequestDto memberDto, Model model) {
        model.addAttribute("memberDto", memberDto);
        return "usr/member/join";
    }

    @PostMapping("/join")
    public String join (@Valid MemberRequestDto memberDto, BindingResult bindingResult, Model model) {
        RsData<Member> validateRsData = memberService.validateMember(memberDto.getLoginID(), memberDto.getEmail());

        if (bindingResult.hasErrors() || validateRsData.isFail() ) {
            if (validateRsData.isFail()){
                switch (validateRsData.getResultCode()){
                    case "F-1" -> model.addAttribute("valid_loginID",validateRsData.getMsg());
                    case "F-2" -> model.addAttribute("valid_email",validateRsData.getMsg());
                }

            }
            Map<String, String> validatorResult = memberService.validateHandling(bindingResult);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            model.addAttribute("memberDto",memberDto);
            return "usr/member/join";
        }
        RsData<Member> rsData = memberService.join(memberDto.getLoginID(), memberDto.getPassword(),memberDto.getEmail(), memberDto.getNickname());

        return "redirect:/usr/member/login";
    }

    @GetMapping("/login")
    public String showLogin (@RequestParam(value = "error", required = false)String error,
                              @RequestParam(value = "exception", required = false)String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "usr/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public String showMe(Model model){
        if(rq.isLogin()){
            model.addAttribute("challengeMembers"
                    , challengeMemberService.getByMember(rq.getMember()));
        }
        return "usr/member/me";
    }

    @GetMapping("/store")
    public String getStore(Model model) {
        Map<String, List<Item>> items = new HashMap<>();
        items.put("fonts",itemService.getByType("font"));
        items.put("characters",itemService.getByType("character"));

        model.addAttribute("items", items);
        model.addAttribute("photoService", photoService);

        return "usr/member/store";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/store/buy/{itemId}")
    public String buyItem(@PathVariable("itemId") String itemId,
                          @RequestParam(required = false, defaultValue = "false") boolean equipped,
                          Principal principal){

        Member loginMember = memberService.getByLoginId(principal.getName());

        RsData<Inventory> buyRsData = memberService.buyItem(itemId, loginMember, equipped);

        System.out.println(buyRsData.getMsg());

        return "redirect:/usr/member/store";
    }

}

