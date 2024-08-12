package com.gudgo.jeju.domain.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Time walkingTime;

    private String profileImageUrl;

    private String ranking;


    public Profile withWalkingTime(Time walkingTime) {
        return Profile.builder()
                .id(this.id)
                .walkingTime(walkingTime != null ? walkingTime : this.walkingTime)
                .profileImageUrl(this.profileImageUrl)
                .ranking(this.ranking)
                .build();
    }

    public Profile withProfileImageUrl(String profileImageUrl) {
        return Profile.builder()
                .id(this.id)
                .walkingTime(this.walkingTime)
                .profileImageUrl(profileImageUrl != null ? profileImageUrl : this.profileImageUrl)
                .ranking(this.ranking)
                .build();
    }

    public Profile withUserRank(String userRank) {
        return Profile.builder()
                .id(this.id)
                .walkingTime(this.walkingTime)
                .profileImageUrl(this.profileImageUrl)
                .ranking(userRank != null ? userRank : this.ranking)
                .build();
    }
}
