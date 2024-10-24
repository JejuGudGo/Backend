package com.example.jejugudgo.global.exception.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String retCode;

    private String retMessage;

    private String alertMessage;

    public ApiResponse updateAlertMessage(String alertMessage) {
        return ApiResponse.builder()
                .id(this.id)
                .retCode(this.retCode)
                .retMessage(this.retMessage)
                .alertMessage(alertMessage)
                .build();
    }
}
