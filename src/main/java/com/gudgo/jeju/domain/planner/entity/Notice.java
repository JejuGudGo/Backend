package com.gudgo.jeju.domain.planner.entity;

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
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String notice;


    @ManyToOne
    @JoinColumn(name = "chattingId")
    private Chatting chatting;

    @OneToOne
    @JoinColumn(name = "participantId")
    private Participant participant;
}
