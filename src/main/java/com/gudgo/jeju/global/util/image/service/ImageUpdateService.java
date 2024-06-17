package com.gudgo.jeju.global.util.image.service;

import com.gudgo.jeju.global.util.image.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ImageUpdateService {
    private final ImageSettingService imageSettingService;

    public Path saveImage(Long userId, MultipartFile multipartFile, Category category) throws Exception {
        if (multipartFile.isEmpty()) {
            throw new MultipartException("IMAGE_01");
        }

        Path rootLocation = imageSettingService.setImagePath(category);

        // 파일 이름, 확장자 추출
        String oriImageName = multipartFile.getOriginalFilename();
        String fileExtension = "";
        if (oriImageName != null && oriImageName.contains(".")) {
            fileExtension = oriImageName.substring(oriImageName.lastIndexOf("."));
        }

        // 파일명 생성 (유저 ID와 현재 시각을 포함)
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        String imageName = userId + "_" + timestamp + fileExtension;
        Path imageUrl = rootLocation.resolve(Paths.get(imageName))
                .normalize().toAbsolutePath();

        // 파일 저장
        try {
            Files.copy(multipartFile.getInputStream(), imageUrl, StandardCopyOption.REPLACE_EXISTING);
            return imageUrl;

        } catch (IOException e) {
            //TODO: 예외 확인하기
            throw new Exception("파일 저장 중 오류가 발생 했습니다: " + e.getMessage());
        }
    }
}
