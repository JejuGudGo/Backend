package com.example.jejugudgo.global.exception.service;

import com.example.jejugudgo.global.exception.entity.ApiResponse;
import com.example.jejugudgo.global.exception.entity.RetCode;
import com.example.jejugudgo.global.exception.repository.ApiResponseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiResponseService {
    private final ApiResponseRepository apiResponseRepository;

    public void loadApiResponse() {
        List<RetCode> retCodes = RetCode.getAllCodes();

        for (RetCode retCode : retCodes) {
            if (apiResponseRepository.findByRetCode(retCode.getRetCode()) == null) {
                ApiResponse apiResponse = ApiResponse.builder()
                        .retCode(retCode.getRetCode())
                        .retMessage(retCode.getMessage())
                        .alertMessage("")
                        .build();

                apiResponseRepository.save(apiResponse);
            }
        }
    }

    @Transactional
    public void updateAlertMessage(RetCode retCode, String alertMessage) {
        ApiResponse apiResponse = apiResponseRepository.findByRetCode(retCode.getRetCode());
        apiResponse = apiResponse.updateAlertMessage(alertMessage);
        apiResponseRepository.save(apiResponse);
    }
}
