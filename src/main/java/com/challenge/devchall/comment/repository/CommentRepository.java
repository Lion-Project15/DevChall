package com.challenge.devchall.comment.repository;

import com.challenge.devchall.challengepost.entity.ChallengePost;
import com.challenge.devchall.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>{

    List<Comment> findByChallengePost(ChallengePost challengePost);

}
