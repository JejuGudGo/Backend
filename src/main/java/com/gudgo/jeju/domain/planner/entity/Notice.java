package com.gudgo.jeju.domain.planner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private boolean isDeleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    @ManyToOne
    @JoinColumn(name = "chatRoomId")
    private ChatRoom chatRoom;

    @OneToOne
    @JoinColumn(name = "participantId")
    private Participant participant;


    public Notice withContent(String content) {
        return Notice.builder()
                .id(this.id)
                .content(content)
                .createdAt(this.createdAt)
                .isDeleted(this.isDeleted)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Notice withIsDeleted() {
        return Notice.builder()
                .id(this.id)
                .content(this.content)
                .isDeleted(true)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
