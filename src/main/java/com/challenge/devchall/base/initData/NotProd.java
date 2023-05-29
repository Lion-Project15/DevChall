package com.challenge.devchall.base.initData;

import com.challenge.devchall.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService
    ) {
        return args -> {
            memberService.join("admin","1234", "admin@admin.com", "관리자", "관리자"); //admin 계정
            memberService.join("user1", "1234","user1@devchall.com", "user1", "user1");
            memberService.join("user2", "1234","user2@devchall.com", "user2", "user2");
            memberService.join("user3", "1234","user3@devchall.com","user3","user3");
            memberService.join("user4", "1234", "user4@devchall.com","user4","user4");

        };
    }
}