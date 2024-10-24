package com.example.jejugudgo.global.exception.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)  // null 값을 제외하고 반환
public class ApiResponse<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String retCode;

    private String retMessage;

    private String alertMessage;

    @Transient
    private T data;


    public ApiResponse<T> updateAlertMessage(String alertMessage) {
        return ApiResponse.<T>builder()
                .id(this.id)
                .retCode(this.retCode)
                .retMessage(this.retMessage)
                .alertMessage(alertMessage)
                .data(this.data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .retCode("00")
                .retMessage("요청에 성공하였습니다.")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(RetCode retCode) {
        return ApiResponse.<T>builder()
                .retCode(retCode.getRetCode())
                .retMessage(retCode.getMessage())
                .build();
    }
}