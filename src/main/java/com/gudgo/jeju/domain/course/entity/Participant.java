package com.gudgo.jeju.domain.course.entity;

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
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String numberTag;

    private String profileImageUrl;

    private boolean approval;

    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;


    private void setApproval(boolean approval) {
        this.approval = approval;
    }

    private void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
