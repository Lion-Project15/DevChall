package com.challenge.devchall.point.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.challengeMember.entity.ChallengeMember;
import com.challenge.devchall.member.entity.Member;
import jakarta.persistence.*;
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
public class Point extends BaseEntity {

    private Long currentPoint;
    private Long totalPoint;

    public void add(int cost){
        this.currentPoint+=cost;
        this.totalPoint+=cost;
        System.out.printf("참가 비용 %d 원이 추가되었습니다.", cost);
    }

    public void subtract(int cost){
        this.currentPoint-=cost;
        System.out.printf("참가 비용 %d 원이 지불되었습니다.", cost);
    }

}
