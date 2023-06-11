package com.challenge.devchall.challange.service;

import com.challenge.devchall.challengeMember.role.Role;
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
    public RsData<Challenge> createChallenge(String title, String contents, boolean status, String frequency, String startDate, String period,
                                             String language, String subject, String postType, MultipartFile file, Member member) throws IOException {

        //1차 검증. 멤버가 한달에 한개 생성 제한에 걸리지 않는가?
        RsData<Member> memberRsData = memberService.checkChallengeLimit(member);

        //이미 이번 달에 챌린지를 생성한 상태라면 뒤의 작업이 이루어지지 않아야 함.
        if (memberRsData.isFail()) {
            System.out.println(memberRsData.getMsg());
            return RsData.of(memberRsData.getResultCode(), memberRsData.getMsg());
        }

        FormattingResult formattingResult = formatting(frequency, startDate, period);

        //2차 검증, 챌린지 생성 룰이 지켜졌는지 검사
        RsData<Challenge> checkRsData = checkCreateRule(title, formattingResult.formattingStartDate, contents, file);

        String photoUrl = "";

        if (checkRsData.isSuccess() && checkRsData.getResultCode().equals("S-1")) {
            //이미지가 있는 경우 이미지 리사이징, 경로 할당
            photoUrl = photoService.photoUpload(file);
        } else if (checkRsData.getResultCode().equals("S-2")) {
            //이미지 없이 만들 경우, 디폴트로 example1 이미지로 생성
            photoUrl = "https://kr.object.ncloudstorage.com/devchall/devchall_img/example1.png";
        } else {
            //생성 불가 - 제약조건 걸림
            System.out.println(checkRsData.getMsg());
            return checkRsData;
        }

        String largePhoto = photoService.getLargePhoto(photoUrl);
        String smallPhoto = photoService.getSmallPhoto(photoUrl);

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
                .challengePostType(postType)
                .challengeCreator(member.getLoginID())
                .gatherPoints(0)
                .challengeMemberLimit(50)
                .build();

        //현재 안쓰이고 있음.
        //int createCost = challenge.getChallengePeriod() * 50;

        challengeRepository.save(challenge);
        challengeMemberService.addMember(challenge, member, Role.LEADER);

        return RsData.of("S-1", "챌린지 생성에 성공하였습니다!");
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

    public RsData<Challenge> checkCreateRule(String title, LocalDate startDate, String contents, MultipartFile file) {

        List<Challenge> byChallengeName = challengeRepository.findByChallengeName(title);

        if (!byChallengeName.isEmpty()) {
            return RsData.of("F-1", "이미 존재하는 챌린지 명입니다.");
        }

        if (title.length() > 25) {
            return RsData.of("F-2", "제목은 25자 까지만 가능합니다.");
        }

        if (startDate.isBefore(LocalDate.now())) {
            return RsData.of("F-3", "챌린지 시작일은 내일 날짜부터 지정할 수 있습니다.");
        }

        if (contents.length() > 500) {
            return RsData.of("F-4", "챌린지 내용은 500자 까지만 작성 가능합니다.");
        }

        RsData<String> imgRsData = photoService.isImgFile(file.getOriginalFilename());

        if (file.isEmpty()) {
            //파일이 없어도, 생성은 가능하다 + 마지막 검사이므로, 생성 가능(S-1) 리턴
            System.out.println("비었지만 생성이 가능");
            return RsData.of("S-2", "이미지가 없어도 생성은 가능합니다.");
        } else {
            //파일이 있을 경우, 확장자 검사
            if (!file.isEmpty() && imgRsData.isSuccess()) {
                System.out.println(imgRsData.getMsg());
            } else {
                //toast ui warning으로 처리?
                System.out.println(imgRsData.getMsg());
                return RsData.of("F-5", "대표이미지 첨부는 jpg, jpeg, png, gif 확장자만 가능합니다.");
            }
        }

        return RsData.of("S-1", "이미지가 있는 챌린지 생성이 가능합니다!");
    }

    //FIXME (중복코드) 사진이 없어도 생성이 되는 것은 이미 createChallenge에 작성되어 있지만, NotProd 처리를 못했음.
    public Challenge createChallengeForNotProd(String title, String contents, boolean status, String frequency, String startDate, String period,
                                               String language, String subject, String postType, Member member) throws IOException {

        String largePhoto = "http://iztyfajjvmsf17707682.cdn.ntruss.com/devchall_img/example1.png?type=m&w=700&h=400&quality=90&bgcolor=FFFFFF&extopt=3";
        String smallPhoto = "http://iztyfajjvmsf17707682.cdn.ntruss.com/devchall_img/example1.png?type=m&w=200&h=115&quality=90&bgcolor=FFFFFF&extopt=0&anilimit=1";
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
                .challengePostType(postType)
                .challengeCreator(member.getLoginID())
                .gatherPoints(0)
                .challengeMemberLimit(50)
                .build();

        challengeRepository.save(challenge);
        challengeMemberService.addMember(challenge, member, Role.LEADER);

        return challenge;
    }
}
