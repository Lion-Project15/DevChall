package com.challenge.devchall.item.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
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
public class Item extends BaseEntity {

    private String name;
    private String type;

    @ManyToOne
    private Member member;

    public Item(String name, String type, Member member) {
        this.name = name;
        this.type = type;
        this.member = member;

    }


}