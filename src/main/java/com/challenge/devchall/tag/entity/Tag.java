package com.challenge.devchall.tag.entity;

import com.challenge.devchall.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
public class Tag extends BaseEntity {


}
