package com.challenge.devchall.comment.service;

import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import com.challenge.devchall.comment.entity.Comment;
import com.challenge.devchall.comment.repository.CommentRepository;
import com.challenge.devchall.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ChallengeMemberService challengeMemberService;
    private final ChallengePostService challengePostService;

    public Comment write(String contents, long id, Member member) {

        ChallengePost post = challengePostService.getChallengePostById(id);


        ChallengeMember challengeMember = challengeMemberService.getByChallengeAndMember(post.getLinkedChallenge(), member).orElse(null);
        if (challengeMember == null){
            return null; //오류메시지출력 + 토스트 사용
        }

        Comment comment = Comment.builder()
                .commentContent(contents)
                .challengeMember(challengeMember)
                .challengePost(post)
                .build();
        commentRepository.save(comment);
        return comment;
    }
    public List<Comment> findByChallengePost(ChallengePost challengePost){
        return commentRepository.findByChallengePost(challengePost);
    }

}
