package com.gudgo.jeju.domain.event.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String organization;

    private LocalDate startDate;

    private LocalDate finishDate;

    private String imageUrl;

    private String informationUrl;

    @Enumerated(value = EnumType.STRING)
    private EventType type;

    public Event withType(EventType type) {
        return Event.builder()
                .id(id)
                .title(title)
                .organization(organization)
                .startDate(startDate)
                .finishDate(finishDate)
                .imageUrl(imageUrl)
                .informationUrl(informationUrl)
                .type(type)
                .build();
    }
}
