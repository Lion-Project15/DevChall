package com.challenge.devchall.photo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@SuperBuilder
@NoArgsConstructor
@Entity
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String photoUrl;

    public String getLargePhoto(){

        String photoUrl = this.photoUrl;

        String[] split = photoUrl.split("devchall/");

        StringBuilder sb = new StringBuilder();

        sb.append("http://iztyfajjvmsf17707682.cdn.ntruss.com/");
        sb.append(split[1]);
        sb.append("?type=m&w=700&h=400&quality=90&bgcolor=FFFFFF&extopt=3");

        return sb.toString();
    }

    public String getSmallPhoto(){

        String photoUrl = this.photoUrl;

        String[] split = photoUrl.split("devchall/");

        StringBuilder sb = new StringBuilder();

        sb.append("http://iztyfajjvmsf17707682.cdn.ntruss.com/");
        sb.append(split[1]);
        sb.append("?type=m&w=200&h=115&quality=90&bgcolor=FFFFFF&extopt=0&anilimit=1");

        return sb.toString();
    }


}
