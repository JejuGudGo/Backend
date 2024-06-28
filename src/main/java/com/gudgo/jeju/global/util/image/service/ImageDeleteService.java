package com.gudgo.jeju.global.util.image.service;

import com.gudgo.jeju.global.util.image.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ImageDeleteService {
    private final ImageSettingService imageSettingService;

    public void deleteImage(Long userId, Category category) throws IOException {
        Path rootLocation = imageSettingService.setImagePath(category);
        DirectoryStream<Path> stream = Files.newDirectoryStream(rootLocation);

        for (Path path : stream) {
            if (path.getFileName().toString().startsWith(userId + "_")) {
                Files.delete(path);
            }
        }
    }

    public void deleteImageWithUrl(String url) throws IOException {
        Path imageLocation = Paths.get(url).normalize().toAbsolutePath();

        try {
            Files.delete(imageLocation);

        } catch (IOException e) {
            if (Files.notExists(imageLocation)) {
                throw new IOException("File not found: " + imageLocation, e);

            } else {
                throw new IOException("Failed to delete file: " + imageLocation, e);
            }
        }
    }
}
