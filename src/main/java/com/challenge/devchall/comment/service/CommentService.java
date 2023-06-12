package com.challenge.devchall.comment.service;

import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.service.ChallengeService;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.entity.ChallengePost;
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

    @Autowired
    private final CommentRepository commentRepository;
    private final ChallengeService challengeService;
    private final ChallengeMemberService challengeMemberService;

    public Comment write(String contents, long id, Member member) {

        Challenge linkedChallenge = challengeService.getChallengeById(id);

        ChallengeMember challengeMember = challengeMemberService.getByChallengeAndMember(linkedChallenge, member).orElse(null);

        Comment comment = Comment.builder()
                .commentContent(contents)
                .challenge(linkedChallenge)
                .challengeMember(challengeMember)
                .build();
        commentRepository.save(comment);
        return comment;
    }

    public List<Comment> findByChallenge(Challenge challenge){
        return commentRepository.findByChallenge(challenge);
    }

}
