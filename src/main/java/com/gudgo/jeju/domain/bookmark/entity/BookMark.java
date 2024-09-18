package com.gudgo.jeju.domain.bookmark.entity;

import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.trail.entity.Trail;
import com.gudgo.jeju.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne
    @JoinColumn(name = "plannerId")
    private Planner planner;

    @OneToOne
    @JoinColumn(name = "trailId")
    private Trail trail;

    public BookMark withDeleted(Long id, boolean isDeleted) {
        return BookMark.builder()
                .id(id)
                .isDeleted(isDeleted)
                .user(user)
                .planner(planner)
                .trail(trail)
                .build();
    }

}
