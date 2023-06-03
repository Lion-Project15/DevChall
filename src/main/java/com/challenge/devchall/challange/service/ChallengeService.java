package com.challenge.devchall.challange.service;

import com.challenge.devchall.base.roles.ChallengeMember.Role;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.repository.ChallengeRepository;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMemberService challengeMemberService;

    @Transactional
    public void createChallenge(String title, String contents, String status, String frequency, String startDate, String endDate, Member member) {

        FormattingResult formattingResult = formatting(status, frequency, startDate, endDate);

        Challenge challenge = Challenge
                .builder()
                .challengeName(title)
                .challengeContents(contents)
                .challengeStatus(formattingResult.formattingStatus)
                .challengeImg(null)
                .challengeTag(null)
                .challengeFrequency(formattingResult.formattingFrequency)
                .startDate(formattingResult.formattingStartDate)
                .endDate(formattingResult.formattingEndDate)
                .build();

        challengeRepository.save(challenge);
        challengeMemberService.addMember(challenge, member, Role.LEADER);
    }

    public List<Challenge> getChallengList() {
        Sort sort = Sort.by(Sort.Direction.ASC, "createDate");
        Pageable pageable = PageRequest.of(0,30,sort);
        return challengeRepository.findAll(pageable).getContent();
    }

    public class FormattingResult {
        private boolean formattingStatus;
        private int formattingFrequency;
        private LocalDate formattingStartDate;
        private LocalDate formattingEndDate;

        public FormattingResult(boolean formattingStatus, int formattingFrequency, LocalDate formattingStartDate, LocalDate formattingEndDate) {
            this.formattingStatus = formattingStatus;
            this.formattingFrequency = formattingFrequency;
            this.formattingStartDate = formattingStartDate;
            this.formattingEndDate = formattingEndDate;
        }
    }

    public FormattingResult formatting(String status, String frequency, String start_date, String end_date){

        boolean formattingStatus = formattingStatus(status);
        int formattingFrequency = formattingFrequency(frequency);
        LocalDate formattingStartDate = formattingDate(start_date);
        LocalDate formattingEndDate = formattingDate(end_date);

        FormattingResult formattingResult = new FormattingResult(formattingStatus, formattingFrequency, formattingStartDate, formattingEndDate);

        return formattingResult;
    }

    public boolean formattingStatus(String status){

        boolean challengeStatus;

        if(status.equals("비공개")) {
            challengeStatus = false;
        }
        else{
            challengeStatus = true;
        }

        return challengeStatus;
    }

    public int formattingFrequency(String frequency){

        int challengeFrequency = 0;

        String[] frequencyData = frequency.split("day");

        challengeFrequency = Integer.parseInt(frequencyData[1]);

        return challengeFrequency;

    }

    public LocalDate formattingDate(String date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = LocalDate.parse(date, formatter);

        return dateTime;
    }

    public Challenge getChallengeById(long id) {

        Challenge challenge = this.challengeRepository.findById(id).orElse(null);

        return challenge;
    }

    public boolean hasPost(Challenge challenge){

        List<ChallengePost> challengePostList = challenge.getChallengePostList();

        if(challengePostList.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    // 챌린지 달성률 체크
    public boolean checkChallengeAchievement(Challenge challenge, int requiredPosts, int unitPeriod) {
        List<ChallengePost> challengePostList = challenge.getChallengePostList();
        int postCount = challengePostList.size();

        // 달성률 계산
        double achievementRate = (double) postCount / requiredPosts;

        // 단위 기간 내에 필요한 인증글 수를 충족하는지 확인
        if (postCount >= requiredPosts) {

            // 단위 기간 동안 달성률을 계산하여 달성 여부 확인
            LocalDate currentDate = LocalDate.now();
            LocalDate startDate = challenge.getStartDate();
            LocalDate endDate = challenge.getEndDate();

            // 현재 날짜보다 시작 날짜가 이전이거나 같은 경우, 단위 기간 수를 증가시키고 시작 날짜에 단위 기간을 더하기
            int unitPeriodCount = 0;
            while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
                if (startDate.isBefore(currentDate) || startDate.isEqual(currentDate)) {
                    unitPeriodCount++;
                    startDate = startDate.plusDays(unitPeriod);
                } else {
                    break;
                }
            }

            double unitPeriodAchievementRate = (double) requiredPosts / unitPeriodCount;

            if (achievementRate >= unitPeriodAchievementRate) {
                return true; // 챌린지 달성 성공
            }
        }

        return false; // 챌린지 달성 실패
    }
}

