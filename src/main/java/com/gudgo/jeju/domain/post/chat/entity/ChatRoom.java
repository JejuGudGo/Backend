package com.gudgo.jeju.domain.post.chat.entity;

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
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;


    public ChatRoom withTitle(String title) {
        return ChatRoom.builder()
                .id(this.id)
                .title(title)
                .build();
    }
}
