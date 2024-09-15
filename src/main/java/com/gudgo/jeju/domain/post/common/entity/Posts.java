package com.gudgo.jeju.domain.post.common.entity;

import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private PostType postType;

    private String title;

    private String content;

    private LocalDate startDate;

    private LocalTime startAt;

    private Long companionsNum;

    private LocalDateTime createdAt;

    private boolean isFinished;

    private boolean isDeleted;

    private String placeName;

    private double placeLatitude;

    private double placeLonggitude;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne
    @JoinColumn(name="plannerId")
    private Planner planner;




    public Posts withTitle(String title) {
        return Posts.builder()
                .id(id)
                .postType(postType)
                .title(title)
                .content(content)
                .companionsNum(companionsNum)
                .createdAt(createdAt)
                .isFinished(isFinished)
                .isDeleted(isDeleted)
                .user(user)
                .planner(planner)
                .placeName(placeName)
                .placeLatitude(placeLatitude)
                .placeLonggitude(placeLonggitude)
                .build();
    }

    public Posts withContent(String content) {
        return Posts.builder()
                .id(id)
                .postType(postType)
                .title(title)
                .content(content)
                .companionsNum(companionsNum)
                .createdAt(createdAt)
                .isFinished(isFinished)
                .isDeleted(isDeleted)
                .user(user)
                .planner(planner)
                .placeName(placeName)
                .placeLatitude(placeLatitude)
                .placeLonggitude(placeLonggitude)
                .build();
    }

    public Posts withCompanionsNum(Long companionsNum) {
        return Posts.builder()
                .id(id)
                .postType(postType)
                .title(title)
                .content(content)
                .companionsNum(companionsNum)
                .createdAt(createdAt)
                .isFinished(isFinished)
                .isDeleted(isDeleted)
                .user(user)
                .planner(planner)
                .placeName(placeName)
                .placeLatitude(placeLatitude)
                .placeLonggitude(placeLonggitude)
                .build();
    }

    public Posts withIsFinished(boolean isFinished) {
        return Posts.builder()
                .id(id)
                .postType(postType)
                .title(title)
                .content(content)
                .companionsNum(companionsNum)
                .createdAt(createdAt)
                .isFinished(isFinished)
                .isDeleted(isDeleted)
                .user(user)
                .planner(planner)
                .placeName(placeName)
                .placeLatitude(placeLatitude)
                .placeLonggitude(placeLonggitude)
                .build();
    }

    public Posts withIsDeleted(boolean isDeleted) {
        return Posts.builder()
                .id(id)
                .postType(postType)
                .title(title)
                .content(content)
                .companionsNum(companionsNum)
                .createdAt(createdAt)
                .isFinished(isFinished)
                .isDeleted(isDeleted)
                .user(user)
                .planner(planner)
                .placeName(placeName)
                .placeLatitude(placeLatitude)
                .placeLonggitude(placeLonggitude)
                .build();
    }

    public Posts withIsFinishedAndIsDeleted(boolean isFinished, boolean isDeleted) {
        return Posts.builder()
                .id(id)
                .postType(postType)
                .title(title)
                .content(content)
                .companionsNum(companionsNum)
                .createdAt(createdAt)
                .isFinished(isFinished)
                .isDeleted(isDeleted)
                .user(user)
                .planner(planner)
                .placeName(placeName)
                .placeLatitude(placeLatitude)
                .placeLonggitude(placeLonggitude)
                .build();
    }

    public Posts withPlanner(Planner planner) {
        return Posts.builder()
                .id(id)
                .postType(postType)
                .title(title)
                .content(content)
                .companionsNum(companionsNum)
                .createdAt(createdAt)
                .isFinished(isFinished)
                .isDeleted(isDeleted)
                .user(user)
                .planner(planner)
                .placeName(placeName)
                .placeLatitude(placeLatitude)
                .placeLonggitude(placeLonggitude)
                .build();
    }

}
