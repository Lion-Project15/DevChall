package com.challenge.devchall.item.entity;

import com.challenge.devchall.base.BaseEntity;
import com.challenge.devchall.inventory.entity.Inventory;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
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
    private String type; //font or character
    private String pattern; //font: 색상 <-> character: url
    private String subPattern; //font: 글색상(잘 안볼 것을 대비)
    private long price;

    @OneToMany(mappedBy = "item")
    private List<Inventory> inventoryList = new ArrayList<>();

}