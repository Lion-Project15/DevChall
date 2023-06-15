package com.challenge.devchall.item.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional

class ItemServiceTest {
    @Autowired
    private ItemService itemService;
    @Test
    @DisplayName("아이템 중복 생성 제한")
    void t01(){
        assertThat(
                itemService.create(
                        "FCE411","font","FCE411", "000000",300).isFail()
        ).isTrue();

    }

    @Test
    @DisplayName("아이템 중복 생성 제한")
    void t02(){
        assertThat(
                itemService.create(
                        "FCE411test","font","FCE411", "000000",300).isFail()
        ).isFalse();

    }


}