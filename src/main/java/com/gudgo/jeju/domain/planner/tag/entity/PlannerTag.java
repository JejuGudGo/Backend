package com.gudgo.jeju.domain.planner.tag.entity;

import com.gudgo.jeju.domain.planner.planner.entity.Planner;
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
public class PlannerTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private PlannerType code;

    @ManyToOne
    @JoinColumn(name = "plannerId")
    private Planner planner;
}
