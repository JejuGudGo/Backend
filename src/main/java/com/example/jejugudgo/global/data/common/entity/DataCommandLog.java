package com.example.jejugudgo.global.data.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataCommandLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String configKey;

    private boolean configValue;

    private LocalDate updatedAt;


    public DataCommandLog updateConfigValue(boolean configValue) {
        return DataCommandLog.builder()
                .id(this.id)
                .configKey(this.configKey)
                .configValue(configValue)
                .updatedAt(this.updatedAt)
                .build();
    }

    public DataCommandLog updateUpdateAt(LocalDate updatedAt) {
        return DataCommandLog.builder()
                .id(this.id)
                .configKey(this.configKey)
                .configValue(this.configValue)
                .updatedAt(updatedAt != null ? updatedAt : this.updatedAt)
                .build();
    }
}