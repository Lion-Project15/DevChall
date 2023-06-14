package com.challenge.devchall.errorController;

import com.challenge.devchall.base.rsData.RsData;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class customErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        RsData.of("F-1", "잘못된 접근입니다.");
        return "redirect:/";
    }
}