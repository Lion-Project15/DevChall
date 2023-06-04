package com.challenge.devchall.challange.service;

import com.challenge.devchall.base.roles.ChallengeMember.Role;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.repository.ChallengeRepository;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
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
    private final MemberService memberService;

    @Transactional
    public void createChallenge(String title, String contents, boolean status, String frequency, String startDate, String period,
                                String language, String subject, String posttype, Member member) {

        RsData<Member> memberRsData = memberService.updateChallengeLimit(member);

        //FIXME 이미 2개를 생성한 상태라면 뒤의 작업이 이루어지지 않아야 함.
        if(memberRsData.isFail()){
            System.out.println(memberRsData.getMsg());
            System.out.println(memberRsData.getMsg());
            System.out.println(memberRsData.getMsg());
            return;
        }

        FormattingResult formattingResult = formatting(frequency, startDate, period);

        Challenge challenge = Challenge
                .builder()
                .challengeName(title)
                .challengeContents(contents)
                .challengeStatus(status)
                .challengeImg(null)
                .challengeFrequency(formattingResult.formattingFrequency)
                .startDate(formattingResult.formattingStartDate)
                .endDate(formattingResult.formattingStartDate.plusWeeks(formattingResult.formattingPeriod))
                .challengePeriod(formattingResult.formattingPeriod)
                .challengeLanguage(language)
                .challengeSubject(subject)
                .challengePostType(posttype)
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
        private int formattingFrequency;
        private LocalDate formattingStartDate;
        private int formattingPeriod;

        public FormattingResult(int formattingFrequency, LocalDate formattingStartDate, int formattingPeriod) {
            this.formattingFrequency = formattingFrequency;
            this.formattingStartDate = formattingStartDate;
            this.formattingPeriod = formattingPeriod;
        }
    }

    public FormattingResult formatting(String frequency, String start_date, String period){

        int formattingFrequency = formattingFrequency(frequency);
        LocalDate formattingStartDate = formattingDate(start_date);
        int formattingPeriod = formattingPeriod(period);

        FormattingResult formattingResult = new FormattingResult( formattingFrequency, formattingStartDate, formattingPeriod);

        return formattingResult;
    }

    public int formattingFrequency(String frequency){

        String[] frequencyData = frequency.split("day");

        return Integer.parseInt(frequencyData[1]);

    }

    public LocalDate formattingDate(String date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = LocalDate.parse(date, formatter);

        return dateTime;
    }

    public int formattingPeriod(String period){

        String[] periodDate = period.split("주");



        return Integer.parseInt(periodDate[0]);
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

}
