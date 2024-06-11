package com.gudgo.jeju.global.data.olle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JeJuOlleCourseData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private Long order;
    
    private double latitude;
    
    private double longitude;
    
    private double altitude;
    
    private OffsetDateTime time;
    
    private LocalDate updatedAt;
    
    
    @ManyToOne
    @JoinColumn(name = "jeJuOlleCourseId")
    private JeJuOlleCourse jeJuOlleCourse;
    
    
    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.time = updatedAt;
    }
}
