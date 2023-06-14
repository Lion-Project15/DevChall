package com.challenge.devchall.challengepost.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    @Value("${custom.challenge.titleLength}")
    private int titleMaxLength;

    @Value("${custom.challenge.contentLength}")
    private int contentsMaxLength;

    private final Map<Long, List<String>> reportedByMap = new HashMap<>();

    public RsData<ChallengePost> write(String title, String contents, boolean status, long postScore, long id,
                                       String photoUrl, Member member) {
        int reportCount = 0;

        Challenge linkedChallenge = challengeService.getChallengeById(id);

//        ChallengeMember challengeMember = challengeMemberService.getByChallengeAndMember(linkedChallenge, member).orElse(null);

        ChallengeMember challengeMember = challengeMemberService.getByChallengeAndMember(linkedChallenge, member)
                .orElse(null);

        List<ChallengePost> posts = getRecentPosts(linkedChallenge, member);

        RsData<ChallengePost> postRsData = canWrite(posts);

        if(postRsData.isFail() || challengeMember == null){
            System.out.println(postRsData.getMsg());
            return postRsData;
        }

        Photo photo = photoService.createPhoto(photoUrl);
        // 제목 길이 제한

        if (title.length() > titleMaxLength) {
            return RsData.of("F-1", "제목은 최대 " + titleMaxLength + "자까지 입력할 수 있습니다.");
        }

        // 내용 길이 제한

        if (contents.length() > contentsMaxLength) {
            return RsData.of("F-1", "내용은 최대 " + contentsMaxLength + "자까지 입력할 수 있습니다.");
        }

        String creatorId = member.getLoginID();

        if(canUpdateTotal(linkedChallenge, member)){
            challengeMember.increaseTotal();
        }

        ChallengePost challengePost = ChallengePost.builder()
                .postTitle(title)
                .postContents(contents)
                .postIsPublic(status)
                .postScore(postScore)
                .linkedChallenge(linkedChallenge)
                .challenger(member)
                .postPhoto(photo)
                .reportCount(reportCount)
                .creatorId(creatorId)
                .build();

        postRsData.setData(challengePostRepository.save(challengePost));

        return postRsData;
    }

    public List<ChallengePost> getRecentPosts(Challenge challenge, Member member){
        return challengePostRepository
                .findByLinkedChallengeAndChallengerOrderByCreateDateDesc(challenge, member);
    }

    public ChallengePost getChallengePostById(long id){

        ChallengePost challengePostById= challengePostRepository.findById(id).orElse(null);

        return challengePostById;
    }

    public List<ChallengePost> getChallengePostByChallenge(Challenge challenge) {
        return challengePostRepository.findByLinkedChallenge(challenge);
    }

    @Transactional
    public void deletePost(long id){

        ChallengePost challengePostById = this.getChallengePostById(id);

        challengePostRepository.delete(challengePostById);
    }

    @Transactional
    public void modifyPost(long id, String title, String contents, boolean status){

        ChallengePost challengePostById = getChallengePostById(id);

        challengePostById.modifyPost(title, contents, status);

    }
    public RsData<ChallengePost> canWrite(List<ChallengePost> posts){
        if((posts != null && posts.size() > 0)
                && !posts.get(0).getCreateDate().toLocalDate().isBefore(LocalDate.now())){

            return RsData.of("F-1", "오늘은 이미 포스트를 작성했습니다.");
        }

        return RsData.of("S-1", "포스트 작성이 가능합니다.");
    }

    private boolean canUpdateTotal(Challenge challenge, Member member){
        List<ChallengePost> posts = getRecentPosts(challenge, member);

        long weeks = (ChronoUnit.DAYS.between(challenge.getStartDate(), LocalDate.now())/7) + 1;
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
