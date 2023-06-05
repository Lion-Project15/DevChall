package com.challenge.devchall.point.schedule;

import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Schedule {
    private int count;
    private int frequency;
    private Set<LocalDate> writtenDates;
    private DayOfWeek challengeStartDay;
    private Challenge challenge;

    @Component
    public class MyScheduledTask {

        private int count; // 초기화할 변수
        private boolean isButtonEnabled; // 버튼의 활성화 여부를 저장하는 변수
        private int frequency; // 챌린지의 frequency

        // 챌린지의 frequency 설정
        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        @Scheduled(cron = "0 0 0 * * *")
        public void resetCount() {
            // 7일 후의 날짜 계산
            LocalDate currentDate = LocalDate.now();
            LocalDate futureDate = currentDate.plusDays(7);

            //오늘 작성한 포스트가 없으면 count 변수 초기화
            //하루에 한 개의 챌린지 포스트만 작성 가능
            if (currentDate.isAfter(futureDate)) {
                count = 0;
            }

            // 글 작성 버튼 활성화 여부 확인
            isButtonEnabled = count < frequency;

            System.out.println("초기화되었습니다.");
        }

        // 버튼의 활성화 여부를 확인
        public boolean isButtonEnabled() {
            return isButtonEnabled;
        }

        // count 증가
        public void increaseCount(boolean isPostComplete) {
            if (isPostComplete) {
                // 글 작성이 완료된 경우
                count++;
            }
            if (count >= frequency) {
                isButtonEnabled = false;
            } else {
                isButtonEnabled = true;
            }
        }

        // 글 작성 완료 시
        public void PostComplete() {
            increaseCount(true);
            if (!isButtonEnabled()) {
                System.out.println("필수 글 작성이 완료되었습니다.");
            }
        }

        // 달성률 계산
        public double getAchievementRate(int totalCount, int unitPeriod) {
            double achievementRate = ((double) count) / (unitPeriod * frequency) * 100;
            return achievementRate;
        }
    }
}
