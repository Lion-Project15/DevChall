package com.challenge.devchall.point.schedule;

import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import com.challenge.devchall.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class Schedule {
    private int count;
    private int frequency;
    private Set<LocalDate> writtenDates;
    private DayOfWeek challengeStartDay;
    private Challenge challenge;
    private final PointService pointService;
    private final MemberService memberService;

    @Component
    public class MyScheduledTask {

        private int count; // 초기화할 변수
        private boolean isButtonEnabled; // 버튼의 활성화 여부를 저장하는 변수
        private int frequency; // 챌린지의 frequency

        // 챌린지의 frequency 설정
        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        @Scheduled(cron = "0 0 0 * * *")//초 분 시간 일 월 요일
        //@Scheduled(cron = "0 * * * * *") //매분 마다
        public void startAccount() {
            pointService.settle();
            // 7일 후의 날짜 계산
            LocalDate currentDate = LocalDate.now();
            LocalDate futureDate = currentDate.plusDays(7);

            // 오늘 작성한 포스트가 없으면 count 변수 초기화
            // 하루에 한 개의 챌린지 포스트만 작성 가능
            if (currentDate.isAfter(futureDate)) {
                count = 0;
            }

            // 글 작성 버튼 활성화 여부 확인
            isButtonEnabled = count < frequency;

            System.out.println("초기화되었습니다.");
        }

        @Scheduled(cron = "0 0 0 1 * *") // 초 분 시간 일 월 요일
        public void resetMonthlyLimit() {
            List<Member> allMembers = memberService.getAllMembers();

            System.out.println("allMembers.get(0) = " + allMembers.get(0));

            for (Member member : allMembers) {
                member.resetChallengeLimit();
            }
        }

        // 달성률 계산
        public double getAchievementRate(int totalCount, int unitPeriod) {
            double achievementRate = ((double) count) / (unitPeriod * frequency) * 100;
            return achievementRate;
        }
    }
}