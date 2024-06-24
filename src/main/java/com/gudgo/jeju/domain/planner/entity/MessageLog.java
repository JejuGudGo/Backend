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
public class MessageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatRoomId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private LogStatus status;

    private String errorMessage;

    private LocalDateTime timestamp;
}