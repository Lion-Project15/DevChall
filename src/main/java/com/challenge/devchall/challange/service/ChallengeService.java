package com.challenge.devchall.challange.service;

import com.challenge.devchall.base.roles.ChallengeMember.Role;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.repository.ChallengeRepository;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import com.challenge.devchall.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMemberService challengeMemberService;
    private final MemberService memberService;
    private final PhotoService photoService;

    @Transactional
    public Challenge createChallenge (String title, String contents, boolean status, String frequency, String startDate, String period,
                                      String language, String subject, String posttype, String photoUrl, Member member) throws IOException {

        RsData<Member> memberRsData = memberService.checkChallengeLimit(member);

        //FIXME 이미 2개를 생성한 상태라면 뒤의 작업이 이루어지지 않아야 함.
        if (memberRsData.isFail()) {
            System.out.println(memberRsData.getMsg());
            return null;
        }

        String largePhoto = photoService.getLargePhoto(photoUrl);
        String smallPhoto = photoService.getSmallPhoto(photoUrl);

        FormattingResult formattingResult = formatting(frequency, startDate, period);

        Challenge challenge = Challenge
                .builder()
                .challengeName(title)
                .challengeContents(contents)
                .challengeStatus(status)
                .largePhoto(largePhoto)
                .smallPhoto(smallPhoto)
                .challengeFrequency(formattingResult.formattingFrequency)
                .startDate(formattingResult.formattingStartDate)
                .endDate(formattingResult.formattingStartDate.plusWeeks(formattingResult.formattingPeriod))
                .challengePeriod(formattingResult.formattingPeriod)
                .challengeLanguage(language)
                .challengeSubject(subject)
                .challengePostType(posttype)
                .challengeCreator(member.getLoginID())
                .gatherPoints(0)
                .challengeMemberLimit(50)
                .build();

        int createCost = challenge.getChallengePeriod() * 50;

        challengeRepository.save(challenge);
        challengeMemberService.addMember(challenge, member, Role.LEADER);
        return challenge;
    }


    public List<Challenge> getChallengList() {
        Sort sort = Sort.by(Sort.Direction.ASC, "createDate");
        Pageable pageable = PageRequest.of(0, 30, sort);
        List<Challenge> challenges = challengeRepository.findByChallengeStatus(true, pageable);
        return challenges;
    }

    public List<Challenge> getChallengeList(String language, String subject) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createDate");
        Pageable pageable = PageRequest.of(0, 30, sort);
        return challengeRepository.findByConditions(language, subject, pageable);
    }

    public List<Challenge> getChallengeList(String language, String subject, Member member) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createDate");
        Pageable pageable = PageRequest.of(0, 30, sort);

        // 현재 사용자가 참여 중인 챌린지 ID 목록을 가져옴
        //List<Long> challengeIds = challengeMemberService.getChallengeIdsByMember(member);

        // 현재 사용자가 참여 중인 챌린지를 제외한 모집 중인 챌린지 목록을 가져옴
        //List<Challenge> challenges = challengeRepository.findByChallengeStatusAndIdNotIn(true, challengeIds, pageable);

        return challengeRepository.findChallengeByNotJoin(language, subject, member, pageable);
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
