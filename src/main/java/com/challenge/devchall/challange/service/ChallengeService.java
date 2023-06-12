package com.challenge.devchall.challange.service;

import com.challenge.devchall.base.roles.ChallengeMember.Role;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.repository.ChallengeRepository;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import com.challenge.devchall.photo.entity.Photo;
import com.challenge.devchall.photo.service.PhotoService;
import com.challenge.devchall.tag.entity.Tag;
import com.challenge.devchall.tag.service.TagService;
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
    private final TagService tagService;

    @Transactional
    public RsData<Challenge> createChallenge(String title, String contents, boolean status, String frequency, String startDate, String period,
                                             String language, String subject, String postType, MultipartFile file, Member member) throws IOException {

        FormattingResult formattingResult = formatting(frequency, startDate, period);

        String photoUrl = "";

        //챌린지 생성 룰이 지켜졌는지 검사
        RsData<Challenge> checkRsData = checkCreateRule(title, formattingResult.formattingStartDate, contents, member);

        //생성 불가 - 제약조건 걸림
        if (checkRsData.isFail()) {
            return checkRsData;
        }


        RsData<String> fileRsData = photoService.isImgFile(file.getOriginalFilename());

        if (fileRsData.isFail()) {

            return RsData.of(fileRsData.getResultCode(), fileRsData.getMsg());

        } else if (fileRsData.getResultCode().equals("S-7")) {

            createChallengeForNoPhoto(title, contents, status, formattingResult.formattingFrequency, formattingResult.formattingStartDate,
                    formattingResult.formattingPeriod, language, subject, postType, member);

        } else if (fileRsData.getResultCode().equals("S-6")) {

            //이미지가 있는 경우 이미지 리사이징, 경로 할당
            photoUrl = photoService.photoUpload(file);

            challengeBuilder(title, contents, status, formattingResult.formattingFrequency, formattingResult.formattingStartDate,
                    formattingResult.formattingPeriod, language, subject, postType, photoUrl, member);
        }

        return RsData.of("S-1", "챌린지 생성에 성공하였습니다!");
    }

    public Challenge createChallengeForNoPhoto(String title, String contents, boolean status, int frequency, LocalDate startDate, int period,
                                               String language, String subject, String postType, Member member) throws IOException {

        String photoUrl = "https://kr.object.ncloudstorage.com/devchall/devchall_img/example1.png";

        return challengeBuilder(title, contents, status, frequency, startDate, period, language, subject, postType, photoUrl, member);
    }

    public Challenge challengeBuilder(String title, String contents, boolean status, int frequency, LocalDate startDate, int period,
                                      String language, String subject, String postType, String photoUrl, Member member) {

        Tag tag = tagService.createTag(language, subject, postType);
        Photo photo = photoService.createPhoto(photoUrl);

        Challenge challenge = Challenge
                .builder()
                .challengeName(title)
                .challengeContents(contents)
                .challengeStatus(status)
                .challengePhoto(photo)
                .challengeFrequency(frequency)
                .startDate(startDate)
                .endDate(startDate.plusWeeks(period))
                .challengePeriod(period)
                .challengeTag(tag)
                .challengeCreator(member.getLoginID())
                .gatherPoints(0)
                .challengeMemberLimit(50)
                .build();

        tag.updateLinkedChallenge(challenge);

        challengeRepository.save(challenge);
        challengeMemberService.addMember(challenge, member, Role.LEADER);
        member.setChallengeLimit(member.getChallengeLimit() + 1);

        return challenge;
    }


    public List<Challenge> getChallengeList() {
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

        //현재 사용자가 참여 중인 챌린지 ID 목록을 가져옴
        List<Long> challengeIds = challengeMemberService.getChallengeIdsByMember(member);

        //현재 사용자가 참여 중인 챌린지를 제외한 모집 중인 챌린지 목록을 가져옴
        List<Challenge> challenges = challengeRepository.findByChallengeStatusAndIdNotIn(true, challengeIds, pageable);

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

    public FormattingResult formatting(String frequency, String start_date, String period) {

        int formattingFrequency = formattingFrequency(frequency);
        LocalDate formattingStartDate = formattingDate(start_date);
        int formattingPeriod = formattingPeriod(period);

        FormattingResult formattingResult = new FormattingResult(formattingFrequency, formattingStartDate, formattingPeriod);

        return formattingResult;
    }

    public int formattingFrequency(String frequency) {

        String[] frequencyData = frequency.split("day");

        return Integer.parseInt(frequencyData[1]);

    }

    public LocalDate formattingDate(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = LocalDate.parse(date, formatter);

        return dateTime;
    }

    public int formattingPeriod(String period) {

        String[] periodDate = period.split("주");

        return Integer.parseInt(periodDate[0]);
    }

    public Challenge getChallengeById(long id) {

        Challenge challenge = this.challengeRepository.findById(id).orElse(null);

        return challenge;
    }

    public boolean hasPost(Challenge challenge) {

        List<ChallengePost> challengePostList = challenge.getChallengePostList();

        if (challengePostList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public RsData<Challenge> checkCreateRule(String title, LocalDate startDate, String contents, Member member) {

        //멤버가 한달에 한개 생성 제한에 걸리지 않는가? (S-1 or F-1)
        RsData<Member> memberRsData = memberService.checkChallengeLimit(member);

        if (memberRsData.isFail()) {
            return RsData.of(memberRsData.getResultCode(), memberRsData.getMsg());
        }

        //이미 존재하는 챌린지 명일 경우
        List<Challenge> byChallengeName = challengeRepository.findByChallengeName(title);

        if (!byChallengeName.isEmpty()) {
            return RsData.of("F-2", "이미 존재하는 챌린지 명입니다.");
        }

        if (title.length() > 25) {
            return RsData.of("F-3", "제목은 25자 까지만 가능합니다.");
        }

        if (startDate.isBefore(LocalDate.now())) {
            return RsData.of("F-4", "챌린지 시작일은 내일 날짜부터 지정할 수 있습니다.");
        }

        if (contents.length() > 500) {
            return RsData.of("F-5", "챌린지 내용은 500자 까지만 작성 가능합니다.");
        }

        return RsData.of("S-1", "챌린지 생성이 가능합니다!");
    }

}
