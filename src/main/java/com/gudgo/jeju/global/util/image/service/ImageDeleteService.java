package com.gudgo.jeju.global.util.image.service;

import com.gudgo.jeju.global.util.image.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ImageDeleteService {
    private final ImageSettingService imageSettingService;
    public void deleteImage(Long userId) throws IOException {
        Path rootLocation = imageSettingService.setImagePath(Category.PROFILE);
        DirectoryStream<Path> stream = Files.newDirectoryStream(rootLocation);

        for (Path path : stream) {
            if (path.getFileName().toString().startsWith(userId + "_")) {
                Files.delete(path);
            }
        }
    }
}
