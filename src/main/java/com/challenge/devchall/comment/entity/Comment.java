package com.challenge.devchall.comment.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Comment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_member_id")
    private ChallengeMember challengeMember;

    @Column(nullable = false)
    private String commentContent;

    private void modifyComment(String commentContent){
        this.commentContent = commentContent;
    }

    @Builder
    public Comment(Challenge challenge, String commentContent, ChallengeMember challengeMember) {
        this.challenge = challenge;
        this.commentContent = commentContent;
        this.challengeMember = challengeMember;
    }

}
