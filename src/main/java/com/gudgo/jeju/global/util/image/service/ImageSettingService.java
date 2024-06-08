package com.gudgo.jeju.global.util.image.service;

import com.gudgo.jeju.global.util.image.entity.Category;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageSettingService {
    public Path setImagePath(Category category) throws IOException {
        // 디렉토리 경로 설정
        Path rootLocation = Paths.get(System.getProperty("user.home") + "/images/" + category.toString().toLowerCase());
        // 해당 경로의 디렉토리가 없다면 생성
        Files.createDirectories(rootLocation);

        return rootLocation;
    }
}
