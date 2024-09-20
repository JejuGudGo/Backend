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

    private Long badgeCount;

    private Long walkingCount;

//    public Profile withWalkingTime(Time walkingTime) {
//        return Profile.builder()
//                .id(this.id)
//                .walkingTime(walkingTime != null ? walkingTime : this.walkingTime)
//                .profileImageUrl(this.profileImageUrl)
//                .ranking(this.ranking)
//                .build();
//    }

    public Profile withProfileImageUrl(String profileImageUrl) {
        return Profile.builder()
                .id(id)
                .walkingTime(walkingTime)
                .profileImageUrl(profileImageUrl != null ? profileImageUrl : this.profileImageUrl)
                .badgeCount(badgeCount)
                .walkingCount(walkingCount)
                .build();
    }

    public Profile withWalkingTime(Time walkingTime) {
        return Profile.builder()
                .id(id)
                .walkingTime(walkingTime)
                .profileImageUrl(profileImageUrl != null ? profileImageUrl : this.profileImageUrl)
                .badgeCount(badgeCount)
                .walkingCount(walkingCount)
                .build();
    }
    public Profile withWalkingCount(Long walkingCount) {
        return Profile.builder()
                .id(id)
                .walkingTime(walkingTime)
                .profileImageUrl(profileImageUrl != null ? profileImageUrl : this.profileImageUrl)
                .badgeCount(badgeCount)
                .walkingCount(walkingCount)
                .build();
    }


    public Profile withBadgeCount(Long badgeCount) {
        return Profile.builder()
                .id(id)
                .walkingTime(walkingTime)
                .profileImageUrl(profileImageUrl != null ? profileImageUrl : this.profileImageUrl)
                .badgeCount(badgeCount)
                .walkingCount(walkingCount)
                .build();
    }

}
