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
import com.challenge.devchall.photo.service.PhotoService;
import com.challenge.devchall.point.entity.Point;
import com.challenge.devchall.point.schedule.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengePostService {

    private final ChallengePostRepository challengePostRepository;
    private final ChallengeService challengeService;
    private final ChallengeMemberService challengeMemberService;
    private final ChallengeMemberRepository challengeMemberRepository;
    private final PhotoService photoService;

    public RsData<ChallengePost> write(String title, String contents, boolean status, long postScore, long id,
                                       String photoUrl, Member member) {

        Challenge linkedChallenge = challengeService.getChallengeById(id);

        ChallengeMember challengeMember = challengeMemberService.getByChallengeAndMember(linkedChallenge, member).orElse(null);

        RsData<ChallengeMember> postLimitRsData = challengeMember.updatePostLimit();


        if (postLimitRsData.isFail()) {
            System.out.println(postLimitRsData.getMsg());
            return null;
        }

        // 제목 길이 제한
        int titleMaxLength = 25;
        if (title.length() > titleMaxLength) {
            return RsData.of("F-1", "제목은 최대 " + titleMaxLength + "자까지 입력할 수 있습니다.");
        }

        // 내용 길이 제한
        int contentsMaxLength = 500;
        if (contents.length() > contentsMaxLength) {
            return RsData.of("F-1", "내용은 최대 " + contentsMaxLength + "자까지 입력할 수 있습니다.");
        }

        String largePhoto = photoService.getLargePhoto(photoUrl);
        String smallPhoto = photoService.getSmallPhoto(photoUrl);

        ChallengePost challengePost = ChallengePost.builder()
                .postTitle(title)
                .postContents(contents)
                .postIsPublic(status)
                .postScore(postScore)
                .linkedChallenge(linkedChallenge)
                .challenger(member)
                .largePhoto(largePhoto)
                .smallPhoto(smallPhoto)
                .build();

        challengePostRepository.save(challengePost);

        return RsData.successOf(challengePost);
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

}
