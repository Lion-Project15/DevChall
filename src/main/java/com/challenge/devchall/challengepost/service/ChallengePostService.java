package com.challenge.devchall.challengepost.service;

import com.challenge.devchall.base.config.AppConfig;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.repository.ChallengeRepository;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.repository.ChallengeMemberRepository;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.challengepost.repository.ChallengePostRepository;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.photo.entity.Photo;
import com.challenge.devchall.photo.service.PhotoService;
import com.challenge.devchall.point.entity.Point;
import com.challenge.devchall.point.schedule.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChallengePostService {

    private final ChallengePostRepository challengePostRepository;
    private final ChallengeService challengeService;
    private final ChallengeMemberService challengeMemberService;
    private final ChallengeMemberRepository challengeMemberRepository;
    private final PhotoService photoService;
    private final Map<Long, List<String>> reportedByMap = new HashMap<>();

    public RsData<ChallengePost> write(String title, String contents, boolean status, long postScore, long id,
                                       String photoUrl, Member member) throws IOException {

        Challenge linkedChallenge = challengeService.getChallengeById(id);

        RsData<String> validationRsData = postValidation(member, title, contents, linkedChallenge);

        if (validationRsData.isFail()) {
            return RsData.of(validationRsData.getResultCode(), validationRsData.getMsg());
        }

        Photo photo = photoService.createPhoto(photoUrl);

        ChallengePost challengePost = ChallengePost.builder()
                .postTitle(title)
                .postContents(contents)
                .postIsPublic(status)
                .postScore(postScore)
                .linkedChallenge(linkedChallenge)
                .challenger(member)
                .postPhoto(photo)
                .reportCount(0)
                .creatorId(member.getLoginID())
                .build();

        challengePostRepository.save(challengePost);

        return RsData.of("S-1", "포스트 작성 성공!");
    }

    public RsData<String> postValidation(Member member, String title, String contents, Challenge linkedChallenge) {

        Optional<ChallengeMember> getChallengeMember = challengeMemberService.getByChallengeAndMember(linkedChallenge, member);

        if (getChallengeMember.isEmpty()) {
            return RsData.of("F-1", "챌린지 참여 여부를 확인할 수 없습니다.");
        }

        ChallengeMember challengeMember = getChallengeMember.get();

        List<ChallengePost> posts = getRecentPosts(linkedChallenge, member);

        RsData<String> checkLimitRsData = checkTodayLimit(posts);

        if (checkLimitRsData.isFail()) {
            return checkLimitRsData;    //resultCode: F-2
        }

        // 제목 길이 제한
        if (title.length() > AppConfig.getTitleLength()) {
            return RsData.of("F-3", "제목은 최대 " + AppConfig.getTitleLength() + "자까지 입력할 수 있습니다.");
        }

        // 내용 길이 제한
        if (contents.length() > AppConfig.getContentLength()) {
            return RsData.of("F-4", "내용은 최대 " + AppConfig.getContentLength() + "자까지 입력할 수 있습니다.");
        }

        //이번달 정산에 사용될 수 있는 인증글 작성인지
        if (canUpdateTotal(linkedChallenge, member)) {
            challengeMember.increaseTotal();
        }

        if (member.getLoginID().equals("admin")) {
            return RsData.of("S-0", "관리자 권한입니다.");
        }

        return RsData.of("S-1", "포스트 작성 조건이 올바릅니다.");
    }

    public List<ChallengePost> getRecentPosts(Challenge challenge, Member member) {
        return challengePostRepository
                .findByLinkedChallengeAndChallengerOrderByCreateDateDesc(challenge, member);
    }

    public ChallengePost getChallengePostById(long id) {

        ChallengePost challengePostById = challengePostRepository.findById(id).orElse(null);

        return challengePostById;
    }

    public List<ChallengePost> getChallengePostByChallenge(Challenge challenge) {
        return challengePostRepository.findByLinkedChallenge(challenge);
    }

    public Page<ChallengePost> getPostPageByChallenge(Challenge challenge, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createDate");
        Pageable pageable = PageRequest.of(page, AppConfig.getPageable(), sort);
        return challengePostRepository.findByLinkedChallenge(challenge, pageable);
    }

    @Transactional
    public void deletePost(long id) {

        ChallengePost challengePostById = this.getChallengePostById(id);

        challengePostRepository.delete(challengePostById);
    }

    @Transactional
    public void modifyPost(long id, String title, String contents, boolean status) {

        ChallengePost challengePostById = getChallengePostById(id);

        challengePostById.modifyPost(title, contents, status);

    }

    public RsData<String> checkTodayLimit(List<ChallengePost> posts) {
        if ((posts != null && posts.size() > 0)
                && !posts.get(0).getCreateDate().toLocalDate().isBefore(LocalDate.now())) {

            return RsData.of("F-2", "오늘은 이미 포스트를 작성했습니다.");
        }

        return RsData.of("S-1", "포스트 작성이 가능합니다.");
    }

    private boolean canUpdateTotal(Challenge challenge, Member member) {
        List<ChallengePost> posts = getRecentPosts(challenge, member);

        long weeks = (ChronoUnit.DAYS.between(challenge.getStartDate(), LocalDate.now()) / 7) + 1;
        return posts.size() < challenge.getChallengeFrequency() * weeks;
    }

    @Transactional
    public void incrementCount(long postId) {
        ChallengePost challengePost = getChallengePostById(postId);
        int currentCount = challengePost.getReportCount();
        challengePost.setReportCount(currentCount + 1);


    }

    public boolean hasReportedPost(long postId, String userId) {
        List<String> reportedByList = reportedByMap.getOrDefault(postId, new ArrayList<>());
        return reportedByList.contains(userId);
    }

    public void addReportedBy(long postId, String userId) {
        List<String> reportedByList = reportedByMap.getOrDefault(postId, new ArrayList<>());
        reportedByList.add(userId);
        reportedByMap.put(postId, reportedByList);
    }

}
