package com.challenge.devchall.point.schedule;

import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.point.service.PointService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.config.http.MatcherType.mvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ScheduleTest {

    @Autowired
    private PointService pointService;


//    @Test
//    @DisplayName("resetCount 작동 확인 ")
//    @WithUserDetails("user1")
//    void test001(){
//
//        Challenge challenge = new Challenge();
//
//        // Set the current date to a specific value (e.g., 7 days before the future date)
//        LocalDate currentDate = LocalDate.now().minusDays(7);
//
//        // Call the resetCount() method
//        challenge.resetCount();
//
//        // Check if the count variable is reset to 0 when currentDate is after futureDate
//        Assertions.assertEquals(0, challenge.getCount());
//
//        // Check if the isButtonEnabled variable is updated correctly
//        Assertions.assertFalse(example.isButtonEnabled());
//    }
}