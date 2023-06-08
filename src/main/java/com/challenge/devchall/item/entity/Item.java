package com.challenge.devchall.item.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.inventory.entity.Inventory;
import com.challenge.devchall.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Item extends BaseEntity {

    private String name;
    private String type;
    private String pattern;

    @OneToMany(mappedBy = "item")
    private List<Inventory> inventoryList = new ArrayList<>();

    // item(1)<-(N)inventory(n)->(1)member(n)


}