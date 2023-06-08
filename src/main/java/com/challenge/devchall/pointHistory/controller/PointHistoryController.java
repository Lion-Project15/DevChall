package com.challenge.devchall.pointHistory.controller;

import com.amazonaws.services.ec2.model.PrincipalIdFormat;
import com.challenge.devchall.base.rq.Rq;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import com.challenge.devchall.pointHistory.entity.PointHistory;
import com.challenge.devchall.pointHistory.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;



@Controller
@RequiredArgsConstructor
public class PointHistoryController {
    private final MemberService memberService;

    private final PointHistoryService pointHistoryService;
    private final Rq rq;

    @GetMapping("/usr/point/pointHistory")
    public String getPointHistory(Model model, Principal principal) {
//        Member loginMember = memberService.getByLoginId();
//        List<PointHistory> pointHistoryList = pointHistoryService.getAllPointHistories();

//        Principal <<;
//        Rq <<;

        List<PointHistory> pointHistoryList = pointHistoryService.getPointHistoriesByMember(rq.getMember());
        model.addAttribute("pointHistoryList", pointHistoryList);
        return "/usr/point/pointHistory";
    }
}