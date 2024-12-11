package com.example.jejugudgo.global.util.image;

import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ImageUtil {
    public Path setImagePath(ImageCategory imageCategory) throws IOException {
        // 사용자 홈 디렉토리에 images 디렉토리 생성
        String userHome = System.getProperty("user.home");
        Path rootLocation = Paths.get(userHome + "/images/" + imageCategory.toString());

        // 해당 경로의 디렉토리가 없다면 생성
        Files.createDirectories(rootLocation);

        return rootLocation;
    }

    public Path save(Long userId, MultipartFile multipartFile, ImageCategory imageCategory) throws Exception {
        Path rootLocation = setImagePath(imageCategory);

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
            throw new CustomException(RetCode.RET_CODE93);
        }
    }

    public void delete(String url) throws IOException {
        Path imageLocation = Paths.get(url).normalize().toAbsolutePath();

        try {
            Files.delete(imageLocation);

        } catch (IOException e) {
            if (Files.notExists(imageLocation)) {
                throw new CustomException(RetCode.RET_CODE94);

            } else {
                throw new CustomException(RetCode.RET_CODE95);
            }
        }
    }
}
