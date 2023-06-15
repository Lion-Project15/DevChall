package com.challenge.devchall.pointHistory.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "point_history")
public class PointHistory extends BaseEntity {

    @ManyToOne
    private Member member;
    private long point;
    private String type; // 포인트 증감 유형
    private long currentpoint;

//    private Long amount; // 포인트의 증감량

    public String getCreateDateStr() {

        LocalDateTime time = this.getCreateDate();

        long months = time.getMonthValue();
        long days = time.getDayOfMonth();
        long hours = time.getHour();
        long minutes = time.getMinute();

        return "%d월 %d일 %02d시 %02d분".formatted(months, days, hours, minutes);
    }
}