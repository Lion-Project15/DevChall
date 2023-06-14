package com.challenge.devchall.challange.service;


import com.challenge.devchall.challengeMember.role.Role;
import com.challenge.devchall.base.config.AppConfig;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.repository.ChallengeRepository;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import com.challenge.devchall.photo.entity.Photo;
import com.challenge.devchall.photo.service.PhotoService;
import com.challenge.devchall.tag.entity.Tag;
import com.challenge.devchall.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMemberService challengeMemberService;
    private final MemberService memberService;
    private final PhotoService photoService;
    private final TagService tagService;

    @Transactional
    public RsData<Challenge> createChallenge(String title, String contents, boolean status, int frequency, String startDate, int period,
                                             String language, String subject, String postType, MultipartFile file, Member member) throws IOException {

        LocalDate formattingStartDate = formattingDate(startDate);

        String photoUrl = "";

        //챌린지 생성 룰이 지켜졌는지 검사
        RsData<Challenge> checkRsData = checkCreateRule(
                        title,
                        formattingStartDate,
                        contents,
                        member,
                (long)period
        );

        //생성 불가 - 제약조건 걸림
        if (checkRsData.isFail()) {
            return checkRsData;
        }

        RsData<String> fileRsData = photoService.isImgFile(file.getOriginalFilename());

        if (fileRsData.isFail()) {

            return RsData.of(fileRsData.getResultCode(), fileRsData.getMsg());

        } else if (fileRsData.getResultCode().equals("S-7")) {

            createChallengeForNoPhoto(title, contents, status, frequency, formattingStartDate,
                    period, language, subject, postType, member);

        } else if (fileRsData.getResultCode().equals("S-6")) {

            //이미지가 있는 경우 이미지 리사이징, 경로 할당
            photoUrl = photoService.photoUpload(file);

            challengeBuilder(title, contents, status, frequency, formattingStartDate,
                    period, language, subject, postType, photoUrl, member);
        }

        return RsData.of("S-1", "챌린지 생성에 성공하였습니다!");
    }

     public Challenge createChallengeForNoPhoto(String title, String contents, boolean status, int frequency, LocalDate startDate, int period,
                                               String language, String subject, String postType, Member member) throws IOException {
        if((long) period * AppConfig.getWeeklyPoint()
                > member.getPoint().getCurrentPoint()){
            System.out.println("포인트 부족으로 챌린지 생성에 실패했습니다.");
            return null;
        }
        String photoUrl = "https://kr.object.ncloudstorage.com/devchall/devchall_img/example1.png";

        return challengeBuilder(title, contents, status, frequency, startDate, period, language, subject, postType, photoUrl, member).getData();
    }

    public RsData<Challenge> challengeBuilder(String title, String contents, boolean status, int frequency, LocalDate startDate, int period,
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
                .challengeMemberLimit(AppConfig.getMemberLimit())
                .build();

        tag.updateLinkedChallenge(challenge);

        challengeRepository.save(challenge);
        challengeMemberService.addMember(challenge, member, Role.LEADER);
        member.setChallengeLimit();

        return RsData.of("S-1","챌린지를 성공적으로 생성했습니다.", challenge);
    }

    //FIXME (안 쓰이는 중) => true 인 것을 모두 가져오는 메서드 (카테고리 X)
//    public List<Challenge> getChallengeList() {
//        Sort sort = Sort.by(Sort.Direction.ASC, "createDate");
//        Pageable pageable = PageRequest.of(0, 30, sort);
//        return challengeRepository.findByChallengeStatus(true, pageable);
//    }

    //공개가 true인 모든 챌린지를 가져옴 (+카테고리 필터링 )
    public Page<Challenge> getChallengeList(int page, String language, String subject) {

        Sort sort = Sort.by(Sort.Direction.ASC, "createDate");
        Pageable pageable = PageRequest.of(page, AppConfig.getPageable(), sort);

        return challengeRepository.findByConditions(language, subject, pageable);
    }

    //참여하지 않고 있는 챌린지(모집중인 챌린지) 중에서, 공개가 true 인 것들을 가져옴(+카테고리 필터링 )
    public Page<Challenge> getNotJoinChallengeList(int page, String language, String subject, Member member) {

        Sort sort = Sort.by(Sort.Direction.ASC, "createDate");
        Pageable pageable = PageRequest.of(page, AppConfig.getPageable(), sort);

        return challengeRepository.findChallengeByNotJoin(language, subject, member, pageable);
    }

    public List<Challenge> getJoinChallenge(Member member){

        //FIXME 나의 챌린지는 페이징 몇개?
        Sort sort = Sort.by(Sort.Direction.ASC, "createDate");
        Pageable pageable = PageRequest.of(0, AppConfig.getPageable(), sort);

        return challengeRepository.findJoinChallenge(member);
    }

    public LocalDate formattingDate(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return LocalDate.parse(date, formatter);
    }

    public Challenge getChallengeById(long id) {

        return this.challengeRepository.findById(id).orElse(null);
    }

    public RsData<Challenge> checkCreateRule(String title, LocalDate startDate, String contents, Member member, long challengeFee) {
        if(challengeFee * AppConfig.getWeeklyPoint()
                > member.getPoint().getCurrentPoint()){
            return RsData.of("F-1", "포인트 부족으로 챌린지를 생성할 수 없습니다");
        }

        //멤버가 한달에 한개 생성 제한에 걸리지 않는가? (S-1 or F-1)
        if(member.getLoginID().equals("admin")){
            return RsData.of("S-1", "챌린지 생성이 가능합니다!");
        }
        RsData<Member> memberRsData = memberService.checkChallengeLimit(member);

        if (memberRsData.isFail()) {
            System.out.println(memberRsData.getMsg());
            return RsData.of(memberRsData.getResultCode(), memberRsData.getMsg());
        }

        //이미 존재하는 챌린지 명일 경우
        Optional<Challenge> byChallengeName = challengeRepository.findByChallengeName(title);

        if (byChallengeName.isPresent()) {
            return RsData.of("F-2", "이미 존재하는 챌린지 명입니다.");
        }

        if (title.length() > AppConfig.getTitleLength()) {
            return RsData.of("F-3", "제목은 %d자 까지만 가능합니다.".formatted(AppConfig.getTitleLength()));
        }

        if (startDate.isBefore(LocalDate.now())) {
            return RsData.of("F-4", "챌린지 시작일은 내일 날짜부터 지정할 수 있습니다.");
        }

        if (contents.length() > AppConfig.getContentLength()) {
            return RsData.of("F-5", "챌린지 내용은 %d자 까지만 작성 가능합니다.".formatted(AppConfig.getContentLength()));
        }

        return RsData.of("S-1", "챌린지 생성이 가능합니다!");
    }

}
