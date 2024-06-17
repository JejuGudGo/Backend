package com.gudgo.jeju.domain.course.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String numberTag;

    private String profileImageUrl;

    @Setter
    private boolean approval;

    @Setter
    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

}
