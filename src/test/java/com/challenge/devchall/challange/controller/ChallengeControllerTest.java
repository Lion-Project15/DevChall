package com.challenge.devchall.challange.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ChallengeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("챌린지 생성 페이지(create_form)")
    @WithUserDetails("user1")
    void t001() throws Exception {

        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/challenge/create"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(ChallengeController.class))
                .andExpect(handler().methodName("createChallenge"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        챌린지 생성 페이지
                        """.stripIndent().trim())));
    }

    @Test
    @WithUserDetails("user1")
    @DisplayName("챌린지 생성 완료(create_form -> Post)")
    void t002() throws Exception {

        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/usr/challenge/create")
                        .with(csrf())
                        .param("title", "테스트 챌린지 1번")
                        .param("contents", "1번 테스트 챌린지 내용입니다.")
                        .param("status", "true")
                        .param("frequency", "2")
                        .param("startDate", "2023-08-20")
                        .param("period", "4")
                        .param("language", "C")
                        .param("subject", "개념 공부")
                        .param("postType", "Github")
                        .param("file", "123.png")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(ChallengeController.class))
                .andExpect(handler().methodName("createChallenge"));
//                .andExpect(status().is3xxRedirection()); //FIXME
    }


    @Test
    @WithUserDetails("user1")
    @DisplayName("챌린지 디테일 테스트")
    void t003() throws Exception {

        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/challenge/detail/1"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(ChallengeController.class))
                .andExpect(handler().methodName("showDetail"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        챌린지 디테일 페이지
                        """.stripIndent().trim())));
    }
}