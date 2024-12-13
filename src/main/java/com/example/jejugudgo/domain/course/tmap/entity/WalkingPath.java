package com.example.jejugudgo.domain.course.tmap.entity;

import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoSearchOption;
import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoCourse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkingPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "userJejuGudgoSearchOptionId")
    private UserJejuGudgoSearchOption searchOption;

    @ManyToOne
    @JoinColumn(name = "userJejuGudgoCourseId")
    private UserJejuGudgoCourse userJejuGudgoCourse;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String lineData;
}
